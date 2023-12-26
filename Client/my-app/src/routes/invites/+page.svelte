<script>

    import Header from "$lib/components/Header.svelte";
    import {afterNavigate, goto} from "$app/navigation";
    import InviteComponent from "$lib/components/InviteComponent.svelte";
    import loadingGif from "$lib/images/loading.gif";
    import {Select} from "flowbite-svelte";
    export let data;
    export let error;

    let invites;

    if(data.invites){
        invites = data.invites;
    }

    function handleInviteDestroy(inviteToChange){
        invites = invites.filter(invite => invite !== inviteToChange);

    }

    afterNavigate(() => {
        if(data.error === 401){
            goto("/login");
        }
    })

    let selection = [
        { value: 'PENDING', name: 'Pending' },
        { value: 'DECLINED', name: 'Declined' }
    ];
    let currentSelection = "PENDING";

    function getInvitesByCurrentSelection(){
        fetch('http://localhost:8080/invites?' + new URLSearchParams({inviteState:currentSelection}), {
            method: 'GET',
            headers: {
                'Content-Type': "application/json",

            },
            credentials: "include"
        })
            .then(response=>{
            if (response.status === 200) {
                response.json().then( value =>{
                    invites = value;
                });
            } else if(response.status === 400){
                response.text().then(text => {
                    throw new Error(text);
                });
            } else if(response.status === 401){
                response.text().then(text => {
                    throw new Error(text);
                });
            } else if(response.status === 500){
                response.text().then(text => {
                    throw new Error(text);
                });
            }
        }).catch(error => {
            console.error(error);
        });
    }

</script>

{#await invites}
    <img src="{loadingGif}" alt="">
{:then invites}

    {#if data.error === 204}
        <Header />
        <div class="invitationState">
            <Select class="mt-2" size="sm" placeholder="" items={selection} bind:value={currentSelection} />
        </div>

        <div class="mainDiv">
            <h1>No invites</h1>
        </div>
    {:else if data.error === 401}
        <!--wait for the page to load and then it will redirect-->
    {:else if data.error === 500}
        <Header />
        <p>Internal server error!</p>
    {:else }
        <Header/>
        <div class="invitationState">
            <Select class="max-w-[15vw]" size="sm" placeholder="" items={selection} bind:value={currentSelection} on:change={getInvitesByCurrentSelection}/>
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