<script>
    import {Button, Input, Label} from "flowbite-svelte";
    import {goto} from "$app/navigation";
    import {loggedIn, userEmail} from "$lib/stores.js";
    import {getToastStore} from "@skeletonlabs/skeleton";

    export let BURole;
    export let onChangeName;
    const toastStore = getToastStore();
    let BUEditName = BURole.businessUnit.name;

    let currentUrl = window.location.pathname;
    let fetchUrl = "";

    if(currentUrl === "/companies"){
        fetchUrl = "/updateCompany";
    } else if(currentUrl === "/company/projects"){
        fetchUrl = "/company/updateProject";
    } else if(currentUrl === "/company/project/teams"){
        fetchUrl = "/company/project/updateTeam";
    }

    function editBU(){
        if(!BUEditName) {
            toastStore.trigger({
                message: "Field can't be empty",
                timeout: 3000,
                hoverable: true,
                background: 'bg-red-200 rounded-lg border-2 border-red-300'
            });
            return;
        }

        let updatedBURole = {
            ...BURole.businessUnit,
            name: BUEditName
        };

        fetch('http://localhost:8080' + fetchUrl, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedBURole),
            credentials: "include"
        }).then(response=>{
            if(response.status === 200){
                toastStore.trigger({
                    message: "Successfully changed name!",
                    timeout: 3000,
                    hoverable: true,
                    background: 'bg-green-200 rounded-lg border-2 border-green-300'
                });
                BURole.name = BUEditName;
                onChangeName(BUEditName);
            } else if(response.status === 400){
                response.text().then(data => {
                    toastStore.trigger({
                        message: "Bad request!",
                        timeout: 3000,
                        hoverable: true,
                        background: 'bg-red-200 rounded-lg border-2 border-red-300'
                    });
                });
            } else if(response.status === 401){
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 403){
                toastStore.trigger({
                    message: "No permission!",
                    timeout: 3000,
                    hoverable: true,
                    background: 'bg-red-200 rounded-lg border-2 border-red-300'
                });
            } else if(response.status === 500){
                response.text().then(data => {
                    toastStore.trigger({
                        message: "Something went wrong",
                        timeout: 3000,
                        hoverable: true,
                        background: 'bg-red-200 rounded-lg border-2 border-red-300'
                    });
                });
            }
        }).catch(error => {
            toastStore.trigger({
                message: "Server is offline!",
                timeout: 3000,
                hoverable: true,
                background: 'bg-red-200 rounded-lg border-2 border-red-300'
            });
        });

    }

</script>

<div class="">
    <div class=" mt-5">
        <Label for="buName" class="text-black ml-10">Name of business unit</Label>
        <Input class="text-black ml-10 max-w-[250px]" type="text" id="buName" bind:value={BUEditName} required/>
    </div>
</div>
<div class="">
    <Button class="ml-10 mt-5" color="blue" type="submit" on:click={editBU}>Edit</Button>
</div>

<style lang="scss">

</style>