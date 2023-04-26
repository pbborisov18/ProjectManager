<script>
    import {goto} from "$app/navigation";
    import {userEmail, loggedIn} from "$lib/stores.js";
    import {
        Dropdown,
        DropdownItem,
        NavLi
    } from 'flowbite-svelte'
    import AgileAceLogo from "$lib/images/AlignLogo.png";
    import inviteIcon from "$lib/images/invite.png";
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
        loggedIn.set("false");
        fetch('http://localhost:8080/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: "include"
        })
            .then(response => {
                if (response.status === 200) {
                    loggedIn.set("");
                    goto("/login");
                } else {
                    throw Error("Неуспешно излизане");
                }
            }).catch(error => {
                alert(error);
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

    {#if homePage}
        <div class="landingPage-section">
            <a on:click={redirectToLogin}
               class="clickable get-started text-white font-bold px-6 py-4 rounded outline-none focus:outline-none mr-1 mb-1 bg-blue-700 active:bg-blue-800 uppercase text-sm shadow hover:shadow-lg ease-linear transition-all duration-150"
            >
                Login
            </a>
            <a on:click={redirectToRegister}
               class="clickable get-started text-white font-bold px-6 py-4 rounded outline-none focus:outline-none mr-1 mb-1 bg-blue-700 active:bg-blue-800 uppercase text-sm shadow hover:shadow-lg ease-linear transition-all duration-150"
            >
                Register
            </a>
        </div>
    {/if}

    {#if $loggedIn === "true"}
        <div class="logged-in-section">
            <img class="clickable" src="{inviteIcon}" alt="invite button" draggable="false" on:click={redirectToInvitesPage}>

            <div class="test clickable not-selectable" >
                <span>{$userEmail}</span>
            </div>
            <Dropdown >
                <DropdownItem on:click={logout}>Logout</DropdownItem>
            </Dropdown>
        </div>
    {/if}
</header>

<style lang="scss">


    header {
        //padding: 5px 20px;
        font-family: sans-serif;
        display: flex;
        align-items: center;
        box-shadow: rgba(0, 0, 0, 0.12) 0px 1px 1px;
        //box-shadow: 0px 0px 10px 1px #eae9ea;
        background-color: #eae9ea;
    }

    img {
        width: 50px;
        //margin-right: 10px;
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
        //background-color: red;

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
        //background-color: orange;
        //height: 100%;
        flex: 1;
        text-align: center;
      }

      .test{
        //position: relative;
        //background-color: green;
        height: 7vh;
        margin-right: 3vw;
        display: flex;
        justify-content: center;
        align-items: center;
        text-align: center;
      }
    }



</style>