<script>

    import whiteboardIcon from "$lib/images/rectangle.png";
    import editIcon from "$lib/images/edit.png";
    import leaveIcon from "$lib/images/leave.png";
    import deleteIcon from "$lib/images/delete.png";
    import {team} from "$lib/stores.js";
    import {goto} from "$app/navigation";
    import inviteToCompanyIcon from "$lib/images/invite.png";
    import {Button, Input, Label, Listgroup, Modal} from "flowbite-svelte";
    import {userEmail, loggedIn} from "$lib/stores";

    let buName;

    let leavePopup = false;
    let deletePopup = false;
    let editPopup = false;
    let inviteToTeamPopup = false;
    let alreadyInvited = [];

    let inviteeEmail;


    export let onDestroy;
    export let BURole;

    function leaveBU(){
        fetch('http://localhost:8080/company/project/leaveTeam', {
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
            }else if(response.status === 400){
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

    function deleteBU(){
        fetch('http://localhost:8080/company/project/deleteTeam', {
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
            }else if(response.status === 400){
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

    function editBU(){
        if(!buName){
            alert("The field can't be empty!");
        }else {
            let updatedBURole = {
                ...BURole.businessUnit,
                name: buName
            };

            fetch('http://localhost:8080/company/project/updateTeam', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedBURole),
                credentials: "include"
            }).then(response=>{
                if (response.status === 200) {
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
            });}
    }

    function invitePersonToTeam(){
        fetch('http://localhost:8080/company/project/team/invite', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({teamDTO: BURole.businessUnit,
                receiver: {id:null, email: inviteeEmail}}),
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
    }

    function getAllInvitesByTeam(){

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

    function redirectToWhiteboard(){
        team.set(JSON.stringify(BURole));
        goto(`/company/project/team/whiteboard`);
    }

</script>

<div class="clickable not-selectable BUwindow">
    {#if BURole.role.name === "MANAGER"}
        <!--the manager stuff here-->

        <span on:click={redirectToWhiteboard}>{BURole.businessUnit.name}</span>
        <div style="border-left:1px solid #BBBBBB;height:80%"></div>
        <div class="imageDivs" on:click={redirectToWhiteboard}>
            <img class="clickable not-selectable" src="{whiteboardIcon}" alt="" draggable="false" >
        </div>
        <div style="border-left:1px solid #BBBBBB;height:80%"></div>
        <div class="imageDivs"  on:click={() => editPopup = true}>
            <img class="clickable not-selectable" src="{editIcon}" alt="" draggable="false">
        </div>
        <div style="border-left:1px solid #BBBBBB;height:80%"></div>
        <div class="imageDivs" on:click={() => leavePopup = true}>
            <img class="clickable not-selectable" src="{leaveIcon}" alt="" draggable="false" >
        </div>
        <div style="border-left:1px solid #BBBBBB;height:80%"></div>
        <div class="imageDivs" on:click={() => deletePopup = true}>
            <img class="clickable not-selectable xImage" src="{deleteIcon}" alt="" draggable="false" >
        </div>
    {:else if BURole.role.name === "EMPLOYEE"}
        <!--the employee stuff here-->
        <span>{BURole.businessUnit.name}</span>
        <div style="border-left:1px solid #BBBBBB;height:80%"></div>
        <div class="imageDivs" on:click={redirectToWhiteboard}>
            <img class="clickable not-selectable" src="{whiteboardIcon}" alt="" draggable="false" >
        </div>
        <div style="border-left:1px solid #BBBBBB;height:80%"></div>
        <div class="imageDivs" on:click={() => leavePopup = true}>
            <img class="clickable not-selectable xImage" src="{leaveIcon}" alt="" draggable="false" >
        </div>
    {/if}
</div>

<Modal bind:open={leavePopup} size="xs" autoclose>
    <div class="text-center">
        <svg aria-hidden="true" class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
        Are you sure you want to leave the team?
        <Button color="red" class="mr-2" on:click={leaveBU}>Yes</Button>
        <Button color='alternative'>No</Button>
    </div>
</Modal>

<Modal bind:open={deletePopup} size="xs" autoclose>
    <div class="text-center">
        <svg aria-hidden="true" class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
        Are you sure you want to delete the team?
        <Button color="red" class="mr-2" on:click={deleteBU}>Yes</Button>
        <Button color='alternative'>No</Button>
    </div>
</Modal>

<Modal title="Edit team" bind:open={editPopup} size="XL" autoclose>
    <div class="bodyPopup">

        <div class="editDiv">
            <div class="teamNameLabel">
                <Label for="teamName" class="mb-2">Team name</Label>
                <Input type="text" id="teamName" required >
                    <input class="text-black inputName" type="text" placeholder="{BURole.businessUnit.name}" bind:value={buName} required/>
                </Input>
            </div>

            <img class="clickable not-selectable inviteImg" src="{inviteToCompanyIcon}" alt="" draggable="false"
                 on:click={() => {
                    getAllInvitesByTeam();
                    inviteToTeamPopup = true;
                 }}>

        </div>
        <Button type="submit" on:click={editBU}>Edit</Button>
    </div>
</Modal>

<Modal title="Invite people in {BURole.businessUnit.name}" bind:open={inviteToTeamPopup} size="XL" autoclose>
    <form>
        <div class="grid gap-6 mb-6 md:grid-cols-1">
            {#if alreadyInvited.length > 0}
                <div class="invited text-black">
                    <span>Invited people</span>
                    <Listgroup items="{alreadyInvited}" let:item class="w-48">
                        <div class="parent text-black">
                            <div class="text">
                                {item.receiver.email}
                            </div>
                        </div>
                    </Listgroup>
                </div>
            {/if}

            <div>
                <Label for="teamName" class="mb-2">Email invite to</Label>
                <Input type="text" id="teamName" required>
                    <input type="text" bind:value={inviteeEmail} />
                </Input>
            </div>
            <Button type="submit" on:click={invitePersonToTeam}>Send</Button>
        </div>
    </form>
</Modal>

<style lang="scss">
    :root{
        background-color: #F8F8F8;
    }


    .BUwindow {
        //background-color: rgba(104, 153, 168, 0.99);
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

    .teamNameLabel{
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
    }

    .invited{
        max-height: 40vh;
        overflow-y: auto;
    }

    .text{
        text-align: center;
    }

</style>