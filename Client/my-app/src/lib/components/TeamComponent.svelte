<script>

    import whiteboardIcon from "$lib/images/rectangle.png";
    import leaveIcon from "$lib/images/leave.png";
    import deleteIcon from "$lib/images/delete.png";
    import {team, userEmail, loggedIn} from "$lib/stores.js";
    import {goto} from "$app/navigation";
    import {
        Button,
        Modal,
        Sidebar,
        SidebarGroup,
        SidebarItem,
        SidebarWrapper
    } from "flowbite-svelte";
    import settingsIcon from "$lib/images/settings.png";
    import EditBUComponent from "$lib/components/EditBUComponent.svelte";
    import RoleSettingsComponent from "$lib/components/RoleSettingsComponent.svelte";
    import SettingsInviteComponent from "$lib/components/SettingsInviteComponent.svelte";

    let leavePopup = false;
    let leaveButtonDisable = false;
    let deletePopup = false;
    let deleteButtonDisable = false;
    let settingsPopup = false;

    export let onDestroy;
    export let BURole;

    function leaveBU(){
        leaveButtonDisable = true;
        fetch('http://localhost:8080/company/project/leaveTeam', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(BURole.businessUnit),
            credentials: "include"
        }).then(response=>{
            leaveButtonDisable = false;
            if (response.ok) {
                leavePopup = false;
                onDestroy();
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
            leaveButtonDisable = false;
            // Server died or something
        });
    }

    function deleteBU(){
        deleteButtonDisable = true;
        fetch('http://localhost:8080/company/project/deleteTeam', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(BURole.businessUnit),
            credentials: "include"
        }).then(response=>{
            deleteButtonDisable = false;

            if (response.status === 200) {
                deletePopup = false;
                onDestroy();
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
            deleteButtonDisable = false;
            // Server died or something
        });
    }

    function redirectToWhiteboard(){
        team.set(JSON.stringify(BURole));
        goto(`/company/project/team/whiteboard`);
    }

    function nameChange(name){
        BURole.businessUnit.name = name;
    }

    function openSettings(){
        for(const a of BURole.authorityDTOList){
            if(a.name === "UpdateBU") {
                activeNum = Math.min(activeNum, 1);
                break;
            } else if(a.name === "SeePermissions"){
                activeNum = Math.min(activeNum, 2);
            } else if(a.name === "ChangePermissions"){
                activeNum = Math.min(activeNum, 3);
            } else if(a.name === "ManageSentInvites"){
                activeNum = Math.min(activeNum, 4);
            }
        }
        settingsPopup = true;
    }

    let activeNum = 50;

</script>

<div class="clickable not-selectable BUwindow">

    <span on:click={redirectToWhiteboard}>{BURole.businessUnit.name}</span>
    {#if BURole.authorityDTOList.some(authority => authority.name === "InteractWithWhiteboard")}
        <div style="border-left:1px solid #BBBBBB;height:80%"></div>
        <div class="imageDivs" on:click={redirectToWhiteboard}>
            <img class="clickable not-selectable" src="{whiteboardIcon}" alt="" draggable="false" >
        </div>
    {/if}

    {#if BURole.authorityDTOList.some(a => a.name === "UpdateBU" || a.name === "SeePermissions" || a.name === "ChangePermissions" || a.name === "ManageSentInvites")}
        <div style="border-left:1px solid #BBBBBB;height:80%"></div>
        <div class="imageDivs" on:click={openSettings}>
            <img class="clickable not-selectable" src="{settingsIcon}" alt="" draggable="false">
        </div>
    {/if}

    <div style="border-left:1px solid #BBBBBB;height:80%"></div>
    <div class="imageDivs" on:click={() => leavePopup = true}>
        <img class="clickable not-selectable" src="{leaveIcon}" alt="" draggable="false" >
    </div>

    {#if BURole.authorityDTOList.some(authority => authority.name === "DeleteBU")}
        <div style="border-left:1px solid #BBBBBB;height:80%"></div>
        <div class="imageDivs" on:click={() => deletePopup = true}>
            <img class="clickable not-selectable xImage" src="{deleteIcon}" alt="" draggable="false" >
        </div>
    {/if}

</div>

<Modal bind:open={leavePopup} size="xs" outsideclose>
    <div class="text-center">
        <svg aria-hidden="true" class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
        <h3 class="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">Are you sure you want to leave the team?</h3>
        <Button color='alternative' class="me-2" on:click={() => leavePopup = false}>No</Button>
        <Button color="red" on:click={leaveBU} disabled="{leaveButtonDisable}">Yes</Button>
    </div>
</Modal>

<Modal bind:open={deletePopup} size="xs" outsideclose>
    <div class="text-center">
        <svg aria-hidden="true" class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
        <h3 class="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">Are you sure you want to delete the team?</h3>
        <Button color='alternative' class="me-2" on:click={() => deletePopup = false}>No</Button>
        <Button color="red" on:click={deleteBU} disabled="{deleteButtonDisable}">Yes</Button>
    </div>
</Modal>

<Modal title="Settings for {BURole.businessUnit.name}" bind:open={settingsPopup} size="xl" placement="center">

    <div class="sideBySide">
        <Sidebar class="mr-1 bg-[#F8F8F8]">
            <SidebarWrapper class="bg-[#F8F8F8]">
                <SidebarGroup>
                    {#if BURole.authorityDTOList.some(authority => authority.name === "UpdateBU")}
                        <SidebarItem label="General Settings" active="{activeNum === 1}" on:click={() => activeNum = 1}/>
                    {/if}
                    {#if BURole.authorityDTOList.some(authority => authority.name === "SeePermissions")}
                        <SidebarItem label="Roles" active="{activeNum === 2}" on:click={() => activeNum = 2}/>
                    {/if}
                    <!--Might need to update roles for this-->
                    {#if BURole.authorityDTOList.some(authority => authority.name === "SeePermissions")}
                        <SidebarItem label="Users" active="{activeNum === 3}" on:click={() => activeNum = 3}/>
                    {/if}
                    {#if BURole.authorityDTOList.some(authority => authority.name === "ManageSentInvites")}
                        <SidebarItem label="Invites" active="{activeNum === 4}" on:click={() => activeNum = 4}/>
                    {/if}
                </SidebarGroup>
            </SidebarWrapper>
        </Sidebar>

        <div class="settingsMain">
            {#if activeNum === 1}
                <EditBUComponent onChangeName="{(name) => nameChange(name)}" bind:BURole="{BURole}"/>
            {/if}
            {#if activeNum === 2}
                <RoleSettingsComponent bind:BURole={BURole}/>
            {/if}
            {#if activeNum === 3}
                <!--User role management component-->
            {/if}
            {#if activeNum === 4}
                <SettingsInviteComponent BURole={BURole}/>
            {/if}
        </div>
    </div>

</Modal>

<style lang="scss">

    :root{
        background-color: #F8F8F8;
    }

    .BUwindow {
        background-color: #e7e7e7;
        width: 97vw;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center; /* align items vertically */
        border: 1px solid #BBBBBB;
        min-height: 8vh;

        span {
            flex-basis: 65%;
            flex-grow: 1;
            white-space: nowrap; /* prevent text from wrapping */
            overflow: hidden; /* hide overflow */
            text-overflow: ellipsis; /* show ellipsis for truncated text */
            font-family: Bahnschrift, monospace;
            height: 100%;
            font-size: 35px;
            display: inline-flex;
            align-items: center;
            vertical-align: middle;
            padding-left: 1.5vw;
        }

        .imageDivs {
            flex-basis: calc((100% - 65%) / 4);
            flex-grow: 0;
            max-width: 20%;
            min-width: 5%;
            height: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        img {
            max-width: 40px;
            max-height: 40px;
        }

        .xImage{
            max-width: 35px;
            max-height: 35px;
        }

    }

    .settingsMain{
        border-radius: 2px;
        background-color: #F8F8F8;
        width: 85%;
        height: 80vh;
        border: 0 solid #BBBBBB;
        font-family: sans-serif;
        font-weight: lighter;
        box-shadow: 0 0 1px 1px #BBBBBB;
        overflow-y: auto;
        overflow-x: hidden;
    }

    .settingsRoles{
        display: flex;
        flex-wrap: wrap;
    }

    .sideBySide{
        display: flex;
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

    .bodyPopup{
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .inviteImg{
        height: 40px;
        width: 40px;
        margin-left: 1.5vw;
        margin-top: 3vh;
    }

</style>