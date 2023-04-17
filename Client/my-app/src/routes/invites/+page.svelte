<script>

    import Header from "$lib/components/Header.svelte";
    import {afterNavigate, goto} from "$app/navigation";
    import InviteComponent from "$lib/components/InviteComponent.svelte";
    import NoInviteComponent from "$lib/components/NoInviteComponent.svelte";
    import loadingGif from "$lib/images/loading.gif";
    export let data;
    export let error;
    let invites;

    if(data.invites){
        invites = data.invites;
    }

    function handleInviteDestroy(invite){
        invites = invites.filter(deleteThis => deleteThis !== invite);
    }

    afterNavigate(() => {
        if(data.error === 401){
            goto("/login");
        }
    })

</script>

{#await invites}
    <img src="{loadingGif}" alt="">
{:then invites}

<!--fix that so it keeps what was last set to it-->

    {#if data.error === 204}
        <Header />
        <NoInviteComponent />
    {:else if data.error === 401}
        <!--wait for the page to load and then it will redirect-->
    {:else if data.error === 400}
        <Header />
        <p>Bad request!</p>
    {:else if data.error === 403}
        <Header/>
        <p>Not authorized to do that!</p>
    {:else if data.error === 500}
        <Header />
        <p>Internal server error!</p>
    {:else }
        <Header/>
        <div>
            {#each invites as invite}
                <InviteComponent invite={invite} onDestroy={() => handleInviteDestroy(invite)}/>
            {/each}
        </div>
    {/if}
{/await}

<style>

    div{
        border-radius: 2px;
        background-color: #F8F8F8;
        width: 97vw;
        margin-top: 5vh;
        margin-left: 1.5vw;
        height: 85vh;
        border: 0 solid #BBBBBB;
        font-family: sans-serif;
        font-weight: lighter;
        box-shadow: 0px 0px 1px 1px #BBBBBB;
        display: flex;
        flex-direction: column;
    }
</style>