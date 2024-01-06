<script>
    import {goto} from "$app/navigation";
    import {userEmail, loggedIn} from "$lib/stores.js";
    import {
        Button,
        Dropdown,
        DropdownItem
    } from 'flowbite-svelte'
    import AgileAceLogo from "$lib/images/AlignLogo.png";
    import inviteIcon from "$lib/images/invite.png";
    import toast from "svelte-french-toast";
    import {PUBLIC_BACKEND_URL} from "$lib/Env.js";

    export let homePage = false;

    function redirectToHomepage() {
        if($loggedIn){
            goto("/companies");
        } else {
            goto("/");
        }
    }

    function redirectToInvitesPage(){
        goto("/invites");
    }

    function logout() {
        fetch(PUBLIC_BACKEND_URL + '/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: "include"
        }).then(response => {
                if (response.status === 200) {
                    userEmail.set("");
                    loggedIn.set("");
                    goto("/login");
                } else {
                    response.text().then(data => {
                        toast.error("Something went wrong!");
                    });
                }
        }).catch(error => {
            toast.error("Server is offline!");
        })

    }

    function redirectToLogin(){
        goto("/login");
    }

</script>

<header>
    <img class="not-selectable clickable logo" src="{AgileAceLogo}" alt="logo" on:click={redirectToHomepage} draggable="false">
    <p class="not-selectable clickable text-[2vh] font-medium" on:click={redirectToHomepage}>AgileAce</p>

    {#if homePage && $loggedIn !== "true"}
        <div class="landingPage-section">
            <Button class="bg-blue-500 mr-[1.5vw] rounded-md font-bold" size="lg" on:click={redirectToLogin}>Login</Button>
        </div>
    {/if}

    {#if $loggedIn === "true"}
        <div class="logged-in-section">
            <img class="clickable" src="{inviteIcon}" alt="invite button" draggable="false" on:click={redirectToInvitesPage}>

            <div class="emailDivBox not-selectable" >
                <p class="clickable text-[1.5vh] font-semibold">{$userEmail}</p>
                <Dropdown >
                    <DropdownItem on:click={logout}>Logout</DropdownItem>
                </Dropdown>
            </div>
        </div>
    {/if}
</header>

<style lang="scss">

    header {
        font-family: sans-serif;
        display: flex;
        align-items: center;
        box-shadow: rgba(0, 0, 0, 0.12) 0 1px 1px;
        background-color: #eae9ea;
        height: 7vh;
        min-height: 69px;
    }

    img {
        width: 5vh;
    }

    span {
        font-family: Bahnschrift, monospace;
    }

    .not-selectable {
        -webkit-user-select: none; /* Chrome, Safari, Opera */
        -moz-user-select: none; /* Firefox */
        -ms-user-select: none; /* IE 10+ */
        user-select: none; /* Standard syntax */
    }

    .clickable {
        cursor: pointer;
    }

    .logo{
        margin-left: 1vw;
        margin-right: 1vw;
    }

    .landingPage-section{
        display: flex;
        align-items: center;
        margin-left: auto;

        a{
            background-color: #63b3ed;
            margin-right: 2vw;
        }
    }

    .logged-in-section {
        display: flex;
        align-items: center;
        margin-left: auto;
        height: 7vh;

        img {
            width: 3vh;
            height: 3vh;
            margin-right: 3vw;
        }

        span {
            font-size: 14px;
            font-weight: bold;
            color: #333333;
            margin-right: 30px;
            font-family: Bahnschrift, monospace;
            flex: 1;
            text-align: center;
        }

        .emailDivBox{
            height: 7vh;
            margin-right: 3vw;
            display: flex;
            justify-content: center;
            align-items: center;
            text-align: center;
        }
    }

</style>