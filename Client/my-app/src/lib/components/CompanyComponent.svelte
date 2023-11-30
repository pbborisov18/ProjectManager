<script>

    import {goto} from "$app/navigation";
    import {
        Button,
        Input,
        Label,
        Modal,
        Listgroup, Toast, CloseButton, Sidebar, SidebarItem, SidebarWrapper, SidebarGroup
    } from 'flowbite-svelte'
    import "../../tailwind.css";
    import { company } from "$lib/stores.js";
    import whiteboardIcon from "$lib/images/rectangle.png";
    import leaveIcon from "$lib/images/leave.png";
    import deleteIcon from "$lib/images/delete.png";
    import editIcon from "$lib/images/edit.png";
    import settingsIcon from "$lib/images/settings.png";


    let leavePopup = false;
    let deletePopup = false;
    let editPopup = false;
    let inviteToCompanyPopup = false;
    let alreadyInvited = [];

    export let onDestroy;
    export let BURole;

    let BUEditName = BURole.businessUnit.name;

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

    function leaveBU(){
        fetch('http://localhost:8080/leaveCompany', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(BURole.businessUnit),
            credentials: "include"
        }).then(response=>{
        if (response.ok) {
            onDestroy();
        } else if(response.status === 204){
            response.text().then(text => {
                onDestroy();
            });
        }else if(response.status === 400){
            response.text().then(text => {
                throw new Error(text);
            });
            addNotification("Something went wrong!");
        } else if(response.status === 401){
            response.text().then(text => {
                throw new Error(text);
            });
            goto("/login");
        } else if(response.status === 403){
            response.text().then(text => {
                throw new Error(text);
            });
            addNotification("Something went wrong!");
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

    function deleteBU(){
        fetch('http://localhost:8080/deleteCompany', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(BURole.businessUnit),
            credentials: "include"
        }).then(response=>{
            if (response.ok) {
                onDestroy();
            } else if(response.status === 204){
                response.text().then(text => {
                    onDestroy();
                })
            } else if(response.status === 400){
                response.text().then(text => {
                    throw new Error(text);
                })
            } else if(response.status === 401){
                response.text().then(text => {
                    throw new Error(text);
                });
                goto("/login");
            } else if(response.status === 403){
                response.text().then(text => {
                    throw new Error(text);
                });
            } else if(response.status === 500){
                response.text().then(text => {
                    throw new Error(text);
                });
            }
        }).catch(error => {
            alert(error);
        });
    }

    function editBU(){
        if(!BUEditName){
            alert("The field can't be empty!");
        }else {
            let updatedBURole = {
                ...BURole.businessUnit,
                name: BUEditName
            };
            fetch('http://localhost:8080/updateCompany', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedBURole),
                credentials: "include"
            }).then(response=>{
                if(response.ok){
                    onDestroy();
                } else if (response.status === 201) {
                    onDestroy();
                } else if(response.status === 400){
                    response.text().then(text => {
                        throw new Error(text);
                    })
                } else if(response.status === 401){
                    response.text().then(text => {
                        throw new Error(text);
                    });
                    goto("/login");
                } else if(response.status === 403){
                    response.text().then(text => {
                        throw new Error(text);
                    });
                } else if(response.status === 500){
                    response.text().then(text => {
                        throw new Error(text);
                    });
                }
            }).catch(error => {
                alert(error);
            });
        }
    }

    let inviteeEmail;

    function invitePersonToCompany(){
        fetch('http://localhost:8080/company/invite', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({businessUnitDTO: BURole.businessUnit,
                userNoPassDTO: {id:null, email: inviteeEmail}}),
            credentials: "include"
        }).then(response=>{
            if (response.status === 201) {
                console.log("success");
            } else if(response.status === 400){
                response.text().then(text => {
                    throw new Error(text);
                })
            } else if(response.status === 401){
                response.text().then(text => {
                    throw new Error(text);
                });
                goto("/login");
            } else if(response.status === 403){
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

        inviteeEmail = "";
    }

    function getAllInvitesByCompany(){

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
                response.text().then(text => {
                    throw new Error(text);
                })
            } else if(response.status === 401){
                response.text().then(text => {
                    throw new Error(text);
                });
                goto("/login");
            } else if(response.status === 403){
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

    let clickedInvite;

    function cancelInvite(){
        if(clickedInvite) {
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

                } else if(response.status === 400){
                    response.text().then(text => {
                        throw new Error(text);
                    })
                } else if(response.status === 401){
                    response.text().then(text => {
                        throw new Error(text);
                    });
                    goto("/login");
                } else if(response.status === 500){
                    response.text().then(text => {
                        throw new Error(text);
                    });
                }
            }).catch(error => {
                console.error(error);
            });
        }
    }

    function redirectToProjects(){
        company.set(JSON.stringify(BURole));
        goto(`/company/projects`);
    }

    function redirectToWhiteboard(){
        company.set(JSON.stringify(BURole));
        goto('/company/whiteboard');
    }

    function redirectToSettings(){
        company.set(JSON.stringify(BURole));
        goto('/company/settings');
    }

</script>

<!--{#each notifications as notification}-->
<!--    <div class="notificationDiv">-->
<!--        <Toast simple position="bottom-right">-->
<!--            {notification.message}-->
<!--        </Toast>-->
<!--    </div>-->
<!--{/each}-->

<div class="clickable not-selectable BUwindow">

    <span on:click={redirectToProjects}> {BURole.businessUnit.name} </span>

    {#if BURole.authorityDTOList.some(authority => authority.name === "InteractWithWhiteboard")}
        <div style="border-left:1px solid #BBBBBB;height:80%"/>
        <div class="imageDivs" on:click={redirectToWhiteboard}>
            <img class="clickable not-selectable" src="{whiteboardIcon}" alt="" draggable="false" >
        </div>
    {/if}

    {#if BURole.authorityDTOList.some(authority => authority.name === "UpdateBU")}
        <div style="border-left:1px solid #BBBBBB;height:80%"/>
        <div class="imageDivs" on:click={() => editPopup = true}>
            <img class="clickable not-selectable" src="{settingsIcon}" alt="" draggable="false" >
        </div>
    {/if}

    <div style="border-left:1px solid #BBBBBB;height:80%"/>
    <div class="imageDivs" on:click={() => leavePopup = true}>
        <img class="clickable not-selectable" src="{leaveIcon}" alt="" draggable="false" >
    </div>

    {#if BURole.authorityDTOList.some(authority => authority.name === "DeleteBU")}
        <div style="border-left:1px solid #BBBBBB;height:80%"/>
        <div class="imageDivs" on:click={() => deletePopup = true}>
            <img class="clickable not-selectable xImage" src="{deleteIcon}" alt="" draggable="false" >
        </div>
    {/if}
</div>

<Modal bind:open={leavePopup} size="xs" autoclose outsideclose>
    <div class="text-center">
        <svg aria-hidden="true" class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
        Are you sure you want to leave the company?
        <Button color="red" class="mr-2" on:click={leaveBU}>Yes</Button>
        <Button color='alternative'>No</Button>
    </div>
</Modal>

<Modal bind:open={deletePopup} size="xs" autoclose outsideclose>
    <div class="text-center">
        <svg aria-hidden="true" class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
        Are you sure you want to delete the company?
        <Button color="red" class="mr-2" on:click={deleteBU}>Yes</Button>
        <Button color='alternative'>No</Button>
    </div>
</Modal>

<Modal title="Settings for {BURole.businessUnit.name}" bind:open={editPopup} size="xl" autoclose outsideclose>

        <div class="sideBySide">
            <Sidebar class="mr-1 bg-[#F8F8F8]">
                <SidebarWrapper class="bg-[#F8F8F8]">
                    <SidebarGroup>
                        {#if BURole.authorityDTOList.some(authority => authority.name === "UpdateBU")}
                            <SidebarItem label="General Settings">
                                <!--General settings component-->
                            </SidebarItem>
                        {/if}
                        {#if BURole.authorityDTOList.some(authority => authority.name === "SeePermissions")}
                            <SidebarItem label="Roles">
                                <!--Manager roles component (Will need further checks inside to see if ChangePermissions exists)-->
                            </SidebarItem>
                        {/if}
                        <!--Might need to update roles for this-->
                        {#if BURole.authorityDTOList.some(authority => authority.name === "ChangePermissions")}
                            <SidebarItem label="Users">
                                <!--User role management component-->
                            </SidebarItem>
                        {/if}
                        {#if BURole.authorityDTOList.some(authority => authority.name === "ManageSentInvites")}
                            <SidebarItem label="Invites">
                                <!--Invite component-->
                            </SidebarItem>
                        {/if}
                    </SidebarGroup>
                </SidebarWrapper>
            </Sidebar>

            <div class="settingsMain">

            </div>
        </div>

<!--        <div class="editDiv">-->
<!--            <div class="companyNameLabel">-->
<!--                <Label for="companyName" class="mb-2">Name of the company</Label>-->
<!--                <Input type="text" id="companyName" required >-->
<!--                    <input class="text-black inputName" type="text" bind:value={BUEditName} required/>-->
<!--                </Input>-->
<!--            </div>-->
<!--            {#if BURole.authorityDTOList.some(authority => authority.name === "ManageSentInvites")}-->
<!--                <img class="inviteImg clickable not-selectable" src="{settingsIcon}" alt="" draggable="false"-->
<!--                     on:click={() => {-->
<!--                    getAllInvitesByCompany();-->
<!--                    inviteToCompanyPopup = true;-->
<!--                }}>-->
<!--            {/if}-->
<!--        </div>-->
<!--        <div class="flex flex-col">-->
<!--            <Button color="blue" type="submit" on:click={editBU}>Edit</Button>-->
<!--        </div>-->
<!--    </div>-->
</Modal>

<!--TODO: This will be moved to settings-->
<!--<Modal title="Invite people in {BURole.businessUnit.name}" bind:open={inviteToCompanyPopup} size="sm" outsideclose>-->
<!--    <form>-->
<!--        <div class="grid gap-6 mb-6 md:grid-cols-1">-->
<!--            {#if alreadyInvited.length > 0}-->
<!--                <div class="invited text-black">-->
<!--                    <span>Invited people</span>-->
<!--                    <Listgroup items="{alreadyInvited}" let:item>-->
<!--                        <div class="parent text-black">-->
<!--                            <div class="text">-->
<!--                                {item.receiver.email}-->
<!--                            </div>-->
<!--                            <CloseButton class="close-button" on:click={() => {-->
<!--                                clickedInvite = item;-->
<!--                                cancelInvite();-->
<!--                            }}/>-->
<!--                        </div>-->
<!--                    </Listgroup>-->
<!--                </div>-->
<!--            {/if}-->
<!--            <div class="flex flex-col">-->
<!--                <Label for="projectName" class="mb-2">Email invite to</Label>-->
<!--                <Input type="text" id="projectName" required>-->
<!--                    <input type="text" bind:value={inviteeEmail} />-->
<!--                </Input>-->
<!--            </div>-->


<!--            <Button color="blue" type="submit" on:click={invitePersonToCompany}>Send</Button>-->
<!--        </div>-->
<!--    </form>-->
<!--</Modal>-->

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
      min-height: 80vh;
      border: 0 solid #BBBBBB;
      font-family: sans-serif;
      font-weight: lighter;
      box-shadow: 0 0 1px 1px #BBBBBB;
      overflow-y: auto;
      overflow-x: hidden;
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

  .inputName{
      margin-bottom: 3vh;
  }

  .editDiv{
      display: flex;
      flex-direction: row;
  }

  .inviteImg{
      height: 40px;
      width: 40px;
      margin-left: 1.5vw;
      margin-top: 3vh;
  }

  .companyNameLabel{
      display: flex;
      flex-direction: column
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

  .invited{
      max-height: 40vh;
      overflow-y: auto;
  }

  .text{
      text-align: center;
  }

  .notificationDiv{
      position: absolute;
      height: 80vh;
      width: 100vw;
  }

</style>