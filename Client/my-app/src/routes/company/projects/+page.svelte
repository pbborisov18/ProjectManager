<script>

    import {goto} from "$app/navigation";
    import Header from "$lib/components/Header.svelte";
    import loadingGif from "$lib/images/loading.gif";
    import plusIcon from "$lib/images/plus.png";
    import {Breadcrumb, BreadcrumbItem, Button, Input, Label, Modal} from 'flowbite-svelte';
    import {company, loggedIn, userEmail} from "$lib/stores.js";
    import ProjectComponent from "$lib/components/ProjectComponent.svelte";
    import {onMount} from "svelte";

    let companyBURole = JSON.parse($company);

    let error = 401;
    let BURoles;

    async function getProjects(){
        fetch('http://localhost:8080/company/projects', {
            method: 'POST',
            headers: {
                'Content-Type': "application/json",
            },
            body: JSON.stringify(companyBURole.businessUnit),
            credentials: "include"
        }).then(response=>{
            if (response.status === 200){
                response.json().then( value =>{
                    BURoles = value;
                    error = 200;
                });
            } else if(response.status === 204){
                error = 204;
            } else if(response.status === 400){
                //notification
                //U stoopid bad request
            } else if(response.status === 401){
                //notification
                //Bro why you not logged in
                error = 401;
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if (response.status === 403){
                alert("No permission");
            } else if(response.status === 500){
                // notification
                // addNotification("Something went wrong!");
                // well my backend did something wrong
            }
        }).catch(error => {
            //Server died or something
        });
    }

    function createProject(){
        let project = {id: null,
            name: projectName,
            type: "PROJECT",
            company: companyBURole.businessUnit,
            whiteboard:null
        }

        fetch('http://localhost:8080/company/createProject', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(project),
            credentials: "include"
        }).then(response=>{
            if (response.status === 201){
                //TODO: Make the backend return the object cuz this is a waste
                projectName = "";
                createPopup = false;
                getProjects();
            } else if(response.status === 400){
                //No need to set the error here
                // notification
                // addNotification("Something went wrong!");
            } else if(response.status === 401){
                // notification
                error = 401;
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 403){
                alert("No permission");
            } else if(response.status === 500){
                //No need to set the error here
                // notification
                // addNotification("Something went wrong!");
            }
        }).catch(error => {
            //Server died or something
        });
    }

    function handleBUDestroy(BURole) {
        BURoles = BURoles.filter(remove => remove.id !== BURole.id);
        if(BURoles.length === 0){
            error = 204;
        }
    }

    onMount(() => {
        //Have to do this cuz in getProjects I try to use companyBURole.
        //That will happen if you think you are funny and access /company/projects without
        //"selecting" a company (Probably would also happen if you access it from another page cuz
        //company is in session storage and not local storage).
        //Pretty much impossible to implement with local storage cuz there will be "funky" behavior if you
        //have more than 1 page
        if(companyBURole === null){
            error = 401;
            goto("/companies");
        } else {
            BURoles = getProjects();
        }
    })

    let createPopup = false;
    let projectName;

</script>

{#await BURoles}
    <img src="{loadingGif}" alt="">
{:then BURoles}

    {#if error === 204 && (!BURoles || BURoles.length === 0)}
        <Header/>
        <div class="lowerMenuDiv">
            <Breadcrumb >
                <BreadcrumbItem href="/companies" home>{companyBURole.businessUnit.name}</BreadcrumbItem>
            </Breadcrumb>
            {#if companyBURole.authorityDTOList.some(a => a.name === "CreateChildren")}
                <div class="addProject">
                    <img class="clickable not-selectable" src="{plusIcon}" alt="" draggable="false" on:click={() => createPopup = true}/>
                </div>
            {/if}
        </div>
        {#if companyBURole.authorityDTOList.some(a => a.name === "CreateChildren")}
            <div class="cursor-pointer mainDiv" on:click={() => createPopup = true}>
                <h1 class="not-selectable">You aren't part of any projects in this company.</h1>
                <h1 class="not-selectable">Wait to be invited or make yourself one by clicking here.</h1>
            </div>
            {:else}
            <div class="mainDiv">
                <h1 class="not-selectable">You aren't part of any projects in this company.</h1>
                <h1 class="not-selectable">Wait to be invited.</h1>
            </div>

        {/if}
    {:else if error === 500}
        <Header/>
        <p>Internal server error!</p>
    {:else if error === 401}
        <!--wait for the page to load and then it will redirect-->
    {:else}
        <Header/>
        <div class="lowerMenuDiv">
            <Breadcrumb >
                <BreadcrumbItem href="/companies" home>{companyBURole.businessUnit.name}</BreadcrumbItem>
            </Breadcrumb>
            {#if companyBURole.authorityDTOList.some(a => a.name === "CreateChildren")}
                <div class="addProject">
                    <img class="clickable not-selectable" src="{plusIcon}" alt="" draggable="false" on:click={() => createPopup = true}/>
                </div>
            {/if}
        </div>

        <div class="mainDiv">
            {#each BURoles as BURole}
                <ProjectComponent BURole={BURole} onDestroy={() => handleBUDestroy(BURole)} />
            {/each}
        </div>

    {/if}
{/await}

<Modal title="Create project" bind:open={createPopup} size="xs" outsideclose>
    <form>
        <div class="grid gap-6 mb-6 md:grid-cols-1">
            <div class="flex flex-col">
                <Label for="projectName" class="mb-2">Project name</Label>
                <Input type="text" id="projectName" required bind:value={projectName}/>
            </div>
            <Button color="blue" type="submit" on:click={createProject}>Create</Button>
        </div>
    </form>
</Modal>


<style lang="scss">

    .mainDiv{
        border-radius: 2px;
        background-color: #F8F8F8;
        width: 97vw;
        margin-top: 1vh;
        margin-left: 1.5vw;
        height: 85vh;
        border: 0 solid #BBBBBB;
        display: flex;
        flex-direction: column;
        align-items: center;
        font-family: sans-serif;
        font-weight: lighter;
        box-shadow: 0 0 1px 1px #BBBBBB;
        overflow-y: auto;
        overflow-x: hidden;
    }

    .lowerMenuDiv{
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        margin-top: 1vh;
        margin-left: 1.5vw;

        .addProject{
            margin-right: 1.5vw;
        }
    }

    img{
        width: 3vh;
    }

    .clickable {
        cursor: pointer;
    }

    .not-selectable {
        -webkit-user-select: none; /* Chrome, Safari, Opera */
        -moz-user-select: none; /* Firefox */
        -ms-user-select: none; /* IE 10+ */
        user-select: none; /* Standard syntax */
    }

</style>