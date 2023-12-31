<script>
    import {Button, CloseButton, Input, Listgroup} from "flowbite-svelte";
    import {goto} from "$app/navigation";
    import {onMount} from "svelte";
    import {userEmail, loggedIn} from "$lib/stores";

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
        fetch('http://localhost:8080' + fetchUrl + '/invite', {
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
                //TODO: Bad idea. Should just return the value from the backend when it's created
                getAllInvitesByBU();
                inviteeEmail = "";
            } else if(response.status === 400){
                // notification
            } else if(response.status === 401){
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 403){
                alert("No permission");
            } else if(response.status === 500){
                // notification
            }
        }).catch(error => {
            // server died or something
        });
    }

    function getAllInvitesByBU(){
        fetch('http://localhost:8080/businessUnit/invites', {
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
                // notification
            } else if(response.status === 401){
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 403){
                // notification
            } else if(response.status === 500){
                // notification
            }
        }).catch(error => {
            // server died or something
        });
    }

    // Awful idea as the component gets re-rendered pretty much every time the user clicks the invite "tab"
    onMount(() =>{
        getAllInvitesByBU();
    });

    function cancelInvite(clickedInvite){
        if(!clickedInvite) {
            return;
        }

        clickedInvite = {   ...clickedInvite,
            state:"CANCELLED"}

        fetch("http://localhost:8080/invites", {
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
                // notification
            } else if(response.status === 401){
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 500){
                // notification
            }
        }).catch(error => {
            // server died or something
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