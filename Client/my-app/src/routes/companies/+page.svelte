<script>
    import Header from "$lib/components/Header.svelte";
    import {goto} from "$app/navigation";
    import CompanyComponent from "$lib/components/CompanyComponent.svelte";
    import {Button, Input, Label, Modal} from "flowbite-svelte";
    import loadingGif from "$lib/images/loading.gif";
    import plusIcon from "$lib/images/plus.png";
    import {userEmail, loggedIn} from "$lib/stores.js";
    import {onMount} from "svelte";

    let error = 401;
    let BURoles;

    async function getCompanies(){
        fetch('http://localhost:8080/companies', {
            method: 'GET',
            headers: {
                'Content-Type': "application/json",
            },
            credentials: "include"
        }).then(response=>{
            if (response.status === 200) {
                response.json().then( value =>{
                    BURoles = value.map(({id, businessUnit, authorityDTOList}) => ({id, businessUnit, authorityDTOList}));
                    error = 200;
                });
            } else if(response.status === 204){
                error = 204;
            } else if(response.status === 400){
                //notification
                //U stoopid bad request
            } else if(response.status === 401){
                //notification
                //Bro why you not logged in
                error = 401;
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 500){
                // notification
                // addNotification("Something went wrong!");
                // well my backend died or something
            }
        }).catch(error => {
            //Server died or something
        });
    }

    function createCompany(){
        let company = {id: null,
            name: createBUName,
            type: "COMPANY",
            whiteboard: null
        };

        fetch('http://localhost:8080/createCompany', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(company),
            credentials: "include"
        }).then(response=>{
            if (response.status === 201) {
                //TODO: Make the backend return the object cuz this is a waste
                createBUName = "";
                createPopup = false;
                getCompanies();
            } else if(response.status === 400){
                //No need to set the error here
                // notification
                // addNotification("Something went wrong!");
            } else if(response.status === 401){
                // notification
                error = 401;
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 500){
                //No need to set the error here
                // notification
                // addNotification("Something went wrong!");
            }
        }).catch(error => {
            //Server died or something
        });
    }

    function handleBUDestroy(BURole) {
        BURoles = BURoles.filter(remove => remove.id !== BURole.id);
        if (BURole.length === 0) {
            error = 204;
        }
    }

    onMount(() => {
        BURoles = getCompanies();
    });

    let createPopup = false;
    let createBUName;

    //Notification stuff
    // let notifications = [];
    //
    // function addNotification(message) {
    //
    //     const newNotification = {
    //         message
    //     };
    //
    //     notifications = [...notifications, newNotification];
    //
    //     setTimeout(() => {
    //         removeNotification(newNotification);
    //     }, 5000); // 5000 milliseconds = 5 seconds
    // }
    //
    // function removeNotification(notification) {
    //     notifications = notifications.filter(n => n !== notification);
    // }

</script>

{#await BURoles}
    <img src="{loadingGif}" alt="">
{:then BURoles}
    <!--TODO: Fix the notifications-->
    <!--In the future. A lot of complex shit on how to handle them all coming from different places/pages/files-->
    <!--{#each notifications as notification}-->
    <!--    <div class="notificationDiv">-->
    <!--        <Toast simple position="bottom-right">-->
    <!--            {notification.message}-->
    <!--        </Toast>-->
    <!--    </div>-->
    <!--{/each}-->

    {#if error === 204 && (!BURoles || BURoles.length === 0)}
        <Header/>
        <div class="addCompany">
            <img class="clickable not-selectable" src="{plusIcon}" alt="" draggable="false" on:click={() => createPopup = true}/>
        </div>
        <div class="cursor-pointer mainDiv" on:click={() => createPopup = true}>
            <h1>You aren't part of any companies.</h1>
            <h1>Wait to be invited or make yourself one by clicking here.</h1>
        </div>
    {:else if error === 500}
        <Header />
        <p>Internal server error!</p>
    {:else if error === 401}
        <!--wait for the page to load and then it will redirect-->
    {:else}
        <Header/>
        <div class="addCompany">
            <img class="clickable not-selectable" src="{plusIcon}" alt="" draggable="false" on:click={() => createPopup = true}/>
        </div>

        <div class="mainDiv">
            {#each BURoles as BURole}
                <CompanyComponent BURole={BURole} onDestroy={() => handleBUDestroy(BURole)} />
            {/each}
        </div>
    {/if}

{/await}


<Modal title="Create a company" bind:open={createPopup} size="xs" outsideclose>
    <form>
        <div class="grid gap-6 mb-6 md:grid-cols-1">
            <div class="flex flex-col">
                <Label for="companyName" class="mb-2">Company name</Label>
                <Input type="text" id="companyName" required bind:value={createBUName}/>
            </div>
            <Button color="blue" on:click={createCompany}>Create</Button>
        </div>
    </form>
</Modal>


<style lang="scss">
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

    img{
        width: 3vh;
    }

    .clickable {
        cursor: pointer;
    }

    .not-selectable {
        -webkit-user-select: none; /* Chrome, Safari, Opera */
        -moz-user-select: none; /* Firefox */
        -ms-user-select: none; /* IE 10+ */
        user-select: none; /* Standard syntax */
    }

    .addCompany{
        display: flex;
        flex-direction: row;
        justify-content: flex-end;
        margin-right: 1.5vw;
        margin-top: 1vh;
    }

    .notificationDiv{
        position: absolute;
        height: 100vh;
        width: 100vw;
        background-color: red;
    }

</style>