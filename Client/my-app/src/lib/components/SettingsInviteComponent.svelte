<script>
    import {Button, CloseButton, Input, Listgroup} from "flowbite-svelte";
    import {goto} from "$app/navigation";
    import {onMount} from "svelte";
    import {userEmail, loggedIn} from "$lib/stores";
    import toast from "svelte-french-toast";
    import {PUBLIC_BACKEND_URL} from "$lib/Env.js";

    export let BURole;

    let inviteeEmail;
    let alreadyInvited = [];

    let currentUrl = window.location.pathname;
    let fetchUrl = "";

    if(currentUrl === "/companies"){
        fetchUrl = "/company";
    } else if(currentUrl === "/company/projects"){
        fetchUrl = "/company/project";
    } else if(currentUrl === "/company/project/teams"){
        fetchUrl = "/company/project/team";
    }

    function invitePersonToBU(){
        fetch(PUBLIC_BACKEND_URL + fetchUrl + '/invite', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                businessUnitDTO: BURole.businessUnit,
                userNoPassDTO: {
                    id:null,
                    email: inviteeEmail
                }
            }),
            credentials: "include"
        }).then(response=>{
            if (response.status === 201) {
                response.json().then(data => {
                    alreadyInvited = [...alreadyInvited, data];
                    inviteeEmail = "";
                });
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
                    toast.error("Something went wrong");
                });
            }
        }).catch(error => {
            toast.error("Server is offline!");
        });
    }

    function getAllInvitesByBU(){
        fetch(PUBLIC_BACKEND_URL + '/businessUnit/invites', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(BURole.businessUnit),
            credentials: "include"
        }).then(response=>{
            if (response.status === 200) {
                response.json().then(data => {
                    alreadyInvited = data.filter(obj => obj.state === 'PENDING')
                });
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
                    toast.error("Something went wrong");
                });
            }
        }).catch(error => {
            toast.error("Server is offline!");
        });
    }

    // Awful idea as the component gets re-rendered pretty much every time the user clicks the invite "tab"
    onMount(() =>{
        getAllInvitesByBU();
    });

    function cancelInvite(clickedInvite){
        if(!clickedInvite) {
            toast.error("Haven't selected an invite!");
            return;
        }

        clickedInvite = {   ...clickedInvite,
            state:"CANCELLED"}

        fetch(PUBLIC_BACKEND_URL + "/invites", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(clickedInvite),
            credentials: "include"
        }).then(response=>{
            if (response.status === 200) {
                alreadyInvited = alreadyInvited.filter(invite => invite.id !== clickedInvite.id);
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
                    toast.error("Something went wrong");
                });
            }
        }).catch(error => {
            toast.error("Server is offline!");
        });

    }

</script>

<form>
    <div class="grid gap-6 mb-6 md:grid-cols-1 ml-10 mt-5 mr-10">

        {#if alreadyInvited.length > 0}
            <div class="invited text-black">
                <span>Invited people</span>
                <Listgroup items="{alreadyInvited}" let:item>
                    <div class="parent text-black">
                        <div class="text">
                            {item.receiver.email}
                        </div>
                        <CloseButton class="close-button" on:click={() => cancelInvite(item)}/>
                    </div>
                </Listgroup>
            </div>
        {/if}

        <div class="flex flex-col text-black">
            <span>Email invite to</span>
            <Input  type="text" id="projectName" required bind:value={inviteeEmail}/>
        </div>

        <Button color="blue" type="submit" on:click={invitePersonToBU}>Send</Button>
    </div>
</form>

<style lang="scss">

    .invited{
        max-height: 40vh;
        overflow-y: auto;
    }

    .text{
        text-align: center;
    }

    .close-button{
        position: absolute;
        top: 0;
        right: 0;
        z-index: 1;
    }

    .parent{
        position: relative;
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: center; /* Align child elements horizontally */
        min-width: fit-content;
    }

</style>