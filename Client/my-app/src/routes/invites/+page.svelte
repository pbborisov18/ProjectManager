<script>

    import Header from "$lib/components/Header.svelte";
    import InviteComponent from "$lib/components/InviteComponent.svelte";
    import loadingGif from "$lib/images/loading.gif";
    import {Select} from "flowbite-svelte";
    import {onMount} from "svelte";
    import {loggedIn, userEmail} from "$lib/stores.js";
    import {goto} from "$app/navigation";

    let error = 401;
    let invites;

    function handleInviteDestroy(inviteToChange){
        invites = invites.filter(invite => invite !== inviteToChange);
        if(invites.length === 0){
            error = 204;
        }
    }

    let currentSelection = "PENDING";

    onMount(() => {
        getInvitesByCurrentSelection();
    });

    let selection = [
        { value: 'PENDING', name: 'Pending' },
        { value: 'DECLINED', name: 'Declined' }
    ];

    function getInvitesByCurrentSelection(){
        fetch('http://localhost:8080/invites?' + new URLSearchParams({inviteState:currentSelection}), {
            method: 'GET',
            headers: {
                'Content-Type': "application/json",
            },
            credentials: "include"
        }).then(response=>{
            if (response.status === 200) {
                response.json().then( value =>{
                    error = 200;
                    invites = value;
                });
            } else if(response.status === 204){
                error = 204;
            } else if(response.status === 400){
                //notification
            } else if(response.status === 401){
                error = 401;
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 500){
                // notification
            }
        }).catch(error => {
            //Server's dead or something
        });
    }

</script>


{#await invites}
    <img src="{loadingGif}" alt="">
{:then invites}

    {#if error === 204 && (!invites || invites.length === 0)}
        <Header />
        <div class="invitationState">
            <Select class="mt-2" size="sm" placeholder="" items={selection} bind:value={currentSelection} on:change={getInvitesByCurrentSelection}/>
        </div>

        <div class="mainDiv">
            <h1>No invites</h1>
        </div>
    {:else if error === 401}
        <!--wait for the page to load and then it will redirect-->
    {:else if error === 500}
        <Header />
        <p>Internal server error!</p>
    {:else }
        <Header/>
        <div class="invitationState">
            <Select class="mt-2" size="sm" placeholder="" items={selection} bind:value={currentSelection} on:change={getInvitesByCurrentSelection}/>
        </div>
        <div class="mainDiv">
            {#each invites as invite}
                {#if invite.state === currentSelection}
                    <InviteComponent invite={invite} onDestroy={() => handleInviteDestroy(invite)}/>
                {/if}
            {/each}
        </div>
    {/if}
{/await}

<style>

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

    .invitationState{
        display: flex;
        flex-direction: row;
        justify-content: flex-end;
        margin-right: 25px;
        margin-top: 1vh;
        margin-left: 25px;
    }

</style>