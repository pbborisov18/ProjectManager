<script>

    import {loggedIn, project, userEmail} from "$lib/stores.js";
    import loadingGif from "$lib/images/loading.gif";
    import plusIcon from "$lib/images/plus.png";
    import Header from "$lib/components/Header.svelte";
    import {Breadcrumb, BreadcrumbItem, Button, Input, Label, Modal} from "flowbite-svelte";
    import TeamComponent from "$lib/components/TeamComponent.svelte";
    import {goto} from "$app/navigation";
    import {onMount} from "svelte";

    let projectBURole = JSON.parse($project);

    let error = 401;
    let BURoles;

    async function getTeams(){
        fetch('http://localhost:8080/company/project/teams', {
            method: 'POST',
            headers: {
                'Content-Type': "application/json",
            },
            body: JSON.stringify(projectBURole.businessUnit),
            credentials: "include"
        }).then(response=>{
            if (response.status === 200) {
                response.json().then( value =>{
                    BURoles = value;
                    error = 200;
                });
            } else if(response.status === 204){
                error = 204;
            } else if(response.status === 400){
                //notification
                //bad request
            } else if(response.status === 401){
                //notification
                error = 401;
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if (response.status === 403){
                alert("No permission");
            } else if(response.status === 500){
                // notification
                // well my backend did something wrong
            }
        }).catch(error => {
            //Server died or something
        });
    }

    function createTeam(){
        let team = {
            id: null,
            name: teamName,
            type: "TEAM",
            company: projectBURole.businessUnit.company,
            project: projectBURole.businessUnit,
            whiteboard:null
        }

        fetch('http://localhost:8080/company/project/createTeam', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(team),
            credentials: "include"
        }).then(response=>{
            if (response.status === 201) {
                teamName = "";
                createPopup = false;
                getTeams();
            } else if(response.status === 400){
                //No need to set the error here
                // notification
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
            }
        }).catch(error => {
            //Server died or something
        });
    }

    function handleBUDestroy(BURole) {
        BURoles = BURoles.filter(deleteThis => deleteThis !== BURole);
        if(BURoles.length === 0){
            error = 204;
        }
    }

    onMount(() => {
        if (projectBURole === null) {
            error = 401;
            goto("/company/projects");
        } else {
            BURoles = getTeams();
        }
    });

    let createPopup = false;
    let teamName;


</script>

{#await BURoles}
    <img src="{loadingGif}" alt="">
{:then BURoles}

    {#if error === 204 && (!BURoles || BURoles.length === 0)}
        <Header/>
        <div class="lowerMenuDiv">
            <Breadcrumb >
                <BreadcrumbItem href="/companies" home>{projectBURole?.businessUnit?.company?.name}</BreadcrumbItem>
                <BreadcrumbItem href="/company/projects">{projectBURole?.businessUnit?.name}</BreadcrumbItem>
            </Breadcrumb>
            {#if projectBURole.authorityDTOList.some(a => a.name === "CreateChildren")}
                <div class="addTeam">
                    <img class="clickable not-selectable" src="{plusIcon}" alt="" draggable="false" on:click={() => createPopup = true}/>
                </div>
            {/if}
        </div>
        <div class="cursor-pointer mainDiv" on:click={() => createPopup = true}>
            <h1>You aren't part of any teams in this project.</h1>
            {#if projectBURole.authorityDTOList.some(a => a.name === "CreateChildren")}
                <h1>Wait to be invited or make yourself one by clicking here.</h1>
            {:else}
                <h1>Wait to be invited.</h1>
            {/if}
        </div>
    {:else if error === 500}
        <Header/>
        <p>Internal server error!</p>
    {:else if error === 401}
        <!--wait for the page to load and then it will redirect-->
    {:else}
        <Header/>
        <div class="lowerMenuDiv">
            <Breadcrumb >
                <BreadcrumbItem href="/companies" home>{projectBURole?.businessUnit?.company?.name}</BreadcrumbItem>
                <BreadcrumbItem href="/company/projects">{projectBURole?.businessUnit?.name}</BreadcrumbItem>
            </Breadcrumb>
            {#if projectBURole.authorityDTOList.some(a => a.name === "CreateChildren")}
                <div class="addTeam">
                    <img class="clickable not-selectable" src="{plusIcon}" alt="" draggable="false" on:click={() => createPopup = true}/>
                </div>
            {/if}
        </div>

        <div class="mainDiv">
            {#each BURoles as BURole}
                <TeamComponent BURole={BURole} onDestroy={() => handleBUDestroy(BURole)} />
            {/each}
        </div>

    {/if}

{/await}

<Modal title="Create team" bind:open={createPopup} size="xs" outsideclose>
    <form>
        <div class="grid gap-6 mb-6 md:grid-cols-1">
            <div class="flex flex-col">
                <Label for="teamName" class="mb-2">Team name</Label>
                <Input type="text" id="teamName" required bind:value={teamName}/>
            </div>
            <Button color="blue" type="submit" on:click={createTeam}>Create</Button>
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
        overflow-y: auto;
        overflow-x: hidden;
    }

    .lowerMenuDiv{
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        margin-top: 1vh;
        margin-left: 1.5vw;

        .addTeam{
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