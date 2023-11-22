<script>
    import Header from "$lib/components/Header.svelte";
    import {goto, afterNavigate} from "$app/navigation";
    import CompanyComponent from "$lib/components/CompanyComponent.svelte";
    import {Button, Input, Label, Modal} from "flowbite-svelte";
    import loadingGif from "$lib/images/loading.gif";
    import plusIcon from "$lib/images/plus.png";
    import { Toast } from 'flowbite-svelte';

    export let data;
    export let error;

    let user;
    let BURoles;

    if(data.BURoles){
        BURoles = data.BURoles.map(({id, businessUnit, authorityDTOList}) => ({id, businessUnit, authorityDTOList}));
    }

    function handleBUDestroy(BURole) {
        BURoles = BURoles.filter(deleteThis => deleteThis !== BURole);
        getCompanies();
    }

    afterNavigate(() => {
        if(data.error === 401){
            goto("/login");
        }
    })

    let notifications = [];

    function addNotification(message) {

        const newNotification = {
            message
        };

        notifications = [...notifications, newNotification];

        setTimeout(() => {
            removeNotification(newNotification);
        }, 5000); // 5000 milliseconds = 5 seconds
    }

    function removeNotification(notification) {
        notifications = notifications.filter(n => n !== notification);
    }

    let createPopup = false;
    let value;

    function createCompany(){
        let company = {id: null,
            name: value,
            type: "COMPANY",
            whiteboard: null
        };

        value = "";

        fetch('http://localhost:8080/createCompany', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(company),
            credentials: "include"
        }).then(response=>{
            if (response.status === 201) {
                getCompanies();
            } else if(response.status === 400){
                response.text().then(text => {
                    throw new Error(text);
                })
                addNotification("Something went wrong!");
            } else if(response.status === 401){
                response.text().then(text => {
                    throw new Error(text);
                });
                goto("/login");
            } else if(response.status === 500){
                response.text().then(text => {
                    throw new Error(text);
                });
                addNotification("Something went wrong!");
            }
        }).catch(error => {
            console.error(error);
        });

    }

    function getCompanies(){
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
                });
            } else if(response.status === 400){
                response.text().then(text => {
                    throw new Error(text);
                });
                addNotification("Something went wrong!");
            } else if(response.status === 401){
                response.text().then(text => {
                    throw new Error(text);
                });
                goto("/login");
            } else if(response.status === 500){
                response.text().then(text => {
                    throw new Error(text);
                });
                addNotification("Something went wrong!");
            }
        }).catch(error => {
            console.error(error);
        });
    }

</script>

{#await BURoles}
    <img src="{loadingGif}" alt="">
{:then BURoles}
    <!--TODO: Fix the notifications-->
    <!--{#each notifications as notification}-->
    <!--    <div class="notificationDiv">-->
    <!--        <Toast simple position="bottom-right">-->
    <!--            {notification.message}-->
    <!--        </Toast>-->
    <!--    </div>-->
    <!--{/each}-->

    {#if data.error === 204 && BURoles.length === 0}
        <Header/>
        <div class="addCompany">
            <img class="clickable not-selectable" src="{plusIcon}" alt="" draggable="false" on:click={() => createPopup = true}/>
        </div>
        <div class="cursor-pointer mainDiv" on:click={() => createPopup = true}>
            <h1>You aren't part of any companies.</h1>
            <h1>Wait to be invited or make yourself one by clicking here.</h1>
        </div>
    {:else if data.error === 500}
        <Header />
        <p>Internal server error!</p>
    {:else if data.error === 401}
        <!--wait for the page to load and then it will redirect-->
    {:else}
        <Header/>
        <div class="addCompany">
            <img class="clickable not-selectable" src="{plusIcon}" alt="" draggable="false" on:click={() => createPopup = true}/>
        </div>

        <div class="mainDiv">
            {#each BURoles as BURole}
                <CompanyComponent BURole={BURole}  onDestroy={() => handleBUDestroy(BURole)} />
            {/each}
        </div>
    {/if}

{/await}

<Modal title="Create a company" bind:open={createPopup} size="XL" autoclose>
    <form>
        <div class="grid gap-6 mb-6 md:grid-cols-1">
            <div>
                <Label for="companyName" class="mb-2">Company name</Label>
                <Input type="text" id="companyName" required>
                    <input type="text" bind:value />
                </Input>
            </div>
            <Button type="stu" on:click={createCompany}>Create</Button>
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
        box-shadow: 0px 0px 1px 1px #BBBBBB;
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
    }

</style>