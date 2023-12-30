<script>
    //TODO: Pull out into a separate component
    import Header from "$lib/components/Header.svelte";
    import {Label, Input, Button, Breadcrumb, BreadcrumbItem} from 'flowbite-svelte';
    import {company, project, team} from "$lib/stores.js";
    import {goto} from "$app/navigation";
    import {onMount} from "svelte";

    let companyObj = JSON.parse($company);
    let projectObj = JSON.parse($project);
    let teamObj = JSON.parse($team);

    let currentUrl;

    onMount(() => {
        currentUrl = window.location.pathname;
        if(currentUrl === "/company/project/createWhiteboard"){
            showProjectNav = true;
        }

        if(currentUrl === "/company/project/team/whiteboard"){
            showTeamNav = true;
        }
    });

    let showProjectNav = false;
    let showTeamNav = false;

    let whiteboardName;

    function handleSubmit(){
        let fetchUrl;
        let bu;

        if(currentUrl === "/company/createWhiteboard"){
            fetchUrl = "http://localhost:8080/company/createWhiteboard";
            bu = companyObj;
        } else if(currentUrl === "/company/project/createWhiteboard"){
            fetchUrl = "http://localhost:8080/company/project/createWhiteboard";
            bu = projectObj;
        } else if(currentUrl === "/company/project/team/createWhiteboard"){
            fetchUrl = "http://localhost:8080/company/project/team/createWhiteboard";
            bu = teamObj;
        }

        fetch(fetchUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                whiteboardDTO: {
                    id: null,
                    name: whiteboardName
                },
                businessUnitDTO: bu.businessUnit
            }),
            credentials: "include"
        }).then(response=>{
            if (response.status === 201) {
                goto(currentUrl.replace('/createWhiteboard', '/whiteboard'));
            } else if(response.status === 400){
                // notification
            } else if(response.status === 401){
                // notification
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 500){
                // notification
            }
        }).catch(error => {
            // server dead or something
        });

    }

    function redirectToLastPage(){
        if(currentUrl === "/company/createWhiteboard"){
            goto("/companies");
        } else if(currentUrl === "/company/project/createWhiteboard"){
            goto("/company/projects");
        } else if(currentUrl === "/company/project/team/createWhiteboard"){
            goto("/company/project/teams");
        }
    }

</script>


<Header />
<div class="lowerMenuDiv mt-2 ml-[1.5vw]">
    <Breadcrumb >
        {#if companyObj}
            <BreadcrumbItem href="/companies" home>{companyObj.businessUnit.name}</BreadcrumbItem>
        {/if}
        {#if projectObj && (showProjectNav || showTeamNav)}
            <BreadcrumbItem href="/company/projects">{projectObj.businessUnit.name}</BreadcrumbItem>
        {/if}
        {#if teamObj && showTeamNav}
            <BreadcrumbItem href="/company/project/teams">{teamObj.businessUnit.name}</BreadcrumbItem>
        {/if}
    </Breadcrumb>
</div>
<div class="mainDiv">
    <h2 class="mb-4 text-xl font-bold text-gray-900 dark:text-white">Create whiteboard</h2>
    <form on:submit={handleSubmit}>
        <div class="grid gap-4 sm:grid-cols-2 sm:gap-6">
            <div class="sm:col-span-2">
                <Label for="name" class="mb-2">Name</Label>
                <Input type="text" id="name" placeholder="Whiteboard name" bind:value={whiteboardName} required />
            </div>
            <Button on:click={redirectToLastPage} color="red">Cancel</Button>
            <Button type="submit" class="w-32" color="blue">Create</Button>

        </div>
    </form>
</div>

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
    }
    .lowerMenuDiv{
        display: flex;
        flex-direction: row;
    }
    img{
        width: 30px;
    }
</style>