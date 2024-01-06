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

    function redirectToRegister(){
        goto("/sign-up");
    }

</script>

<header>
    <img class="not-selectable clickable logo" src="{AgileAceLogo}" alt="logo" on:click={redirectToHomepage} draggable="false">
    <span class="not-selectable clickable" on:click={redirectToHomepage}>AgileAce</span>

    {#if homePage && $loggedIn !== "true"}
        <div class="landingPage-section">
            <Button color="blue" on:click={redirectToLogin}>Login</Button>
            <Button color="blue" on:click={redirectToRegister}>Register</Button>
<!--            <a on:click={redirectToLogin}-->
<!--               class="clickable get-started text-white font-bold px-6 py-4 rounded outline-none focus:outline-none mr-1 mb-1 bg-blue-700 active:bg-blue-800 uppercase text-sm shadow hover:shadow-lg ease-linear transition-all duration-150"-->
<!--            >-->
<!--                Login-->
<!--            </a>-->
<!--            <a on:click={redirectToRegister}-->
<!--               class="clickable get-started text-white font-bold px-6 py-4 rounded outline-none focus:outline-none mr-1 mb-1 bg-blue-700 active:bg-blue-800 uppercase text-sm shadow hover:shadow-lg ease-linear transition-all duration-150"-->
<!--            >-->
<!--                Register-->
<!--            </a>-->
        </div>
    {/if}

    {#if $loggedIn === "true"}
        <div class="logged-in-section">
            <img class="clickable" src="{inviteIcon}" alt="invite button" draggable="false" on:click={redirectToInvitesPage}>

            <div class="emailDivBox not-selectable" >
                <span class="clickable">{$userEmail}</span>
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
        width: 50px;
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
            width: 30px;
            height: 30px;
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