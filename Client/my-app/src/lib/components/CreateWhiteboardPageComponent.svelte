<script>
    import Header from "$lib/components/Header.svelte";
    import {Label, Input, Button, Breadcrumb, BreadcrumbItem} from 'flowbite-svelte';
    import {loggedIn, userEmail} from "$lib/stores.js";
    import {goto} from "$app/navigation";
    import {onMount} from "svelte";
    import toast from "svelte-french-toast";
    import {PUBLIC_BACKEND_URL} from "$lib/Env.js";

    export let BURole;

    let whiteboardName;
    let permission = !BURole?.authorityDTOList.some(a => a.name === "CreateWhiteboard");

    function createWhiteboard(){
        fetch(PUBLIC_BACKEND_URL + "/createWhiteboard", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                whiteboardDTO: {
                    id: null,
                    name: whiteboardName
                },
                businessUnitDTO: BURole.businessUnit
            }),
            credentials: "include"
        }).then(response=>{
            if (response.status === 201) {
                goto(currentUrl.replace('/createWhiteboard', '/whiteboard'));
            } else if(response.status === 400){
                response.text().then(data => {
                    toast.error("Bad request!");
                });
            } else if(response.status === 401){
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 403){
                toast.error("No permission!");
            } else if(response.status === 500){
                response.text().then(data => {
                    toast.error("Something went wrong!");
                });
            }
        }).catch(error => {
            toast.error("Server is offline!");
        });

    }

    let currentUrl;

    onMount(() => {
        currentUrl = window.location.pathname;
    });

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
    <Breadcrumb>
        {#if BURole?.businessUnit?.type === "COMPANY"}
            <BreadcrumbItem href="/companies" home>{BURole?.businessUnit?.name}</BreadcrumbItem>
        {:else if BURole.businessUnit.type === "PROJECT" || BURole.businessUnit.type === "TEAM"}
            <BreadcrumbItem href="/companies" home>{BURole.businessUnit.company.name}</BreadcrumbItem>
        {/if}
        {#if BURole.businessUnit.type === "PROJECT"}
            <BreadcrumbItem href="/company/projects">{BURole.businessUnit.name}</BreadcrumbItem>
        {:else if BURole.businessUnit.type === "TEAM"}
            <BreadcrumbItem href="/company/projects">{BURole.businessUnit.project.name}</BreadcrumbItem>
        {/if}
        {#if BURole.businessUnit.type === "TEAM"}
            <BreadcrumbItem href="/company/project/teams">{BURole.businessUnit.name}</BreadcrumbItem>
        {/if}
    </Breadcrumb>
</div>
<div class="mainDiv">
    <h2 class="mb-4 text-xl font-bold text-gray-900 dark:text-white">Create whiteboard</h2>
    <form on:submit={createWhiteboard}>
        <div class="grid gap-4 sm:grid-cols-2 sm:gap-6">
            <div class="sm:col-span-2">
                <Label for="name" class="mb-2">Name</Label>
                <Input type="text" id="name" placeholder="Whiteboard name" bind:value={whiteboardName} required />
            </div>
            <Button on:click={redirectToLastPage} color="red">Cancel</Button>
            <Button type="submit" class="w-32" color="blue" disabled="{permission}">Create</Button>

            {#if permission}
                <p class="col-span-2">No permission</p>
            {/if}

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