<script>
    import {Button, Input, Label} from "flowbite-svelte";
    import {goto} from "$app/navigation";
    import {loggedIn, userEmail} from "$lib/stores.js";

    export let BURole;
    export let onChangeName;
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
            alert("The field can't be empty!");
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
                BURole.name = BUEditName;
                onChangeName(BUEditName);
            } else if(response.status === 400){
                // notification
            } else if(response.status === 401){
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 403){
                //notification
            } else if(response.status === 500){
                //notification
            }
        }).catch(error => {
            //server's dead
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