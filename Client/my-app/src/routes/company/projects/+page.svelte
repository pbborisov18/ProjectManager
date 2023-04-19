<script>

    import {afterNavigate, goto} from "$app/navigation";
    import Header from "$lib/components/Header.svelte";
    import loadingGif from "$lib/images/loading.gif";
    import plusIcon from "$lib/images/plus.png";
    import {Breadcrumb, BreadcrumbItem, Button, Input, Label, Modal} from 'flowbite-svelte';
    import {company} from "$lib/stores.js";
    import ProjectComponent from "$lib/components/ProjectComponent.svelte";

    let companyObj = JSON.parse($company);
    export let data;

    export let error;

    let BURoles;

    if(data.userBURoles){
        BURoles = data.userBURoles.map(({ businessUnit, role }) => ({ businessUnit, role }));
    }

    function handleBUDestroy(BURole) {
        BURoles = BURoles.filter(deleteThis => deleteThis !== BURole);
        getProjects();
    }

    function createProject(){
        let project = {id: null,
            name: projectName,
            type:{
                id: 2,
                name: "PROJECT"
            },
            company: companyObj.businessUnit,
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
            if (response.status === 201) {
                getProjects();
            } else if(response.status === 400){
                response.text().then(text => {
                    throw new Error(text);
                })
            } else if(response.status === 401){
                response.text().then(text => {
                    throw new Error(text);
                });
                goto("/login");
            } else if(response.status === 500){
                response.text().then(text => {
                    throw new Error(text);
                });
            }
        }).catch(error => {
            console.error(error);
        });
    }

    function getProjects(){
        fetch('http://localhost:8080/company/projects', {
            method: 'POST',
            headers: {
                'Content-Type': "application/json",
            },
            body: JSON.stringify(companyObj.businessUnit),
            credentials: "include"
        }).then(response=>{
            if (response.status === 200) {
                response.json().then( value =>{
                    BURoles = value;
                    data.error = 200;
                });
            } else if(response.status === 400){
                response.text().then(text => {
                    throw new Error(text);
                })
            } else if(response.status === 401){
                response.text().then(text => {
                    throw new Error(text);
                });
                goto("/login");
            } else if(response.status === 500){
                response.text().then(text => {
                    throw new Error(text);
                });
            }
        }).catch(error => {
            console.error(error);
        });
    }

    let createPopup = false;
    let projectName;

    afterNavigate(() => {
        if(data.error === 401){
            goto("/login");
        }
    })

</script>

{#await data.userBURoles}
    <img src="{loadingGif}" alt="">
{:then userBURoles}


    {#if data.error === 204}
        <Header/>
        <div class="lowerMenuDiv">
            <Breadcrumb >
                <BreadcrumbItem href="/companies" home>{companyObj.businessUnit.name}</BreadcrumbItem>
            </Breadcrumb>
            {#if companyObj.role.name === "MANAGER"}
                <div class="addProject">
                    <img class="clickable not-selectable" src="{plusIcon}" alt="" draggable="false" on:click={() => createPopup = true}/>
                </div>
            {/if}
        </div>
        <div class="cursor-pointer mainDiv" on:click={() => createPopup = true}>
            <h1>Не си част от никой проект на компанията.</h1>
            {#if companyObj.role.name === "MANAGER"}
                <h1>Изчакай да те поканят или направи като натиснеш тук.</h1>
            {:else if companyObj.role.name === "EMPLOYEE"}
                <h1>Изчакай да те поканят.</h1>
            {/if}
        </div>
    {:else if data.error === 500}
        <Header/>
        <p>Internal server error!</p>

    {:else if data.error === 401}
        <!--wait for the page to load and then it will redirect-->

    {:else if BURoles.length === 0}
        <Header />
        <div class="lowerMenuDiv">
            <Breadcrumb >
                <BreadcrumbItem href="/companies" home>{companyObj.businessUnit.name}</BreadcrumbItem>
            </Breadcrumb>
            {#if companyObj.role.name === "MANAGER"}
                <div class="addProject">
                    <img class="clickable not-selectable" src="{plusIcon}" alt="" draggable="false" on:click={() => createPopup = true}/>
                </div>
            {/if}
        </div>
        <div class="cursor-pointer mainDiv" on:click={() => createPopup = true}>
            <h1>Не си част от никой проект на компанията.</h1>
            {#if companyObj.role.name === "MANAGER"}
                <h1>Изчакай да те поканят или направи като натиснеш тук.</h1>
            {:else if companyObj.role.name === "EMPLOYEE"}
                <h1>Изчакай да те поканят.</h1>
            {/if}
        </div>
    {:else}
        <!--tva e usual stranicata (trq sa uprai)-->
        <Header/>
        <div class="lowerMenuDiv">
            <Breadcrumb >
                <BreadcrumbItem href="/companies" home>{companyObj.businessUnit.name}</BreadcrumbItem>
            </Breadcrumb>
            {#if companyObj.role.name === "MANAGER"}
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

<Modal title="Създаване на проект" bind:open={createPopup} size="XL" autoclose>
    <form>
        <div class="grid gap-6 mb-6 md:grid-cols-1">
            <div>
                <Label for="projectName" class="mb-2">Име на проекта</Label>
                <Input type="text" id="projectName" required>
                    <input type="text" bind:value={projectName} />
                </Input>
            </div>
            <Button type="submit" on:click={createProject}>Създаване</Button>
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
        box-shadow: 0px 0px 1px 1px #BBBBBB;
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