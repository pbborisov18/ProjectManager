<script>
    import whiteboardIcon from "$lib/images/rectangle.png";
    import editIcon from "$lib/images/edit.png";
    import leaveIcon from "$lib/images/leave.png";
    import deleteIcon from "$lib/images/delete.png";
    import {Button, Input, Label, Modal} from "flowbite-svelte";
    import {company, project, team} from "$lib/stores.js";
    import {goto} from "$app/navigation";
    import inviteToCompanyIcon from "$lib/images/invitation.png";

    let buName;

    let leavePopup = false;
    let deletePopup = false;
    let editPopup = false;
    let inviteToProjectPopup = false;

    let inviteeEmail;


    export let onDestroy;
    export let BURole;


    function leaveBU(){
        fetch('http://localhost:8080/company/leaveProject', {
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
        fetch('http://localhost:8080/company/deleteProject', {
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
            alert("Полето не може да е празно!");
        }else {
            let updatedBURole = {
                ...BURole.businessUnit,
                name: buName
            };

            fetch('http://localhost:8080/company/updateProject', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedBURole),
                credentials: "include"
            }).then(response=>{
                if (response.status === 200) {
                    onDestroy();
                    // location.reload();
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

    function invitePersonToProject(){
        fetch('http://localhost:8080/company/project/invite', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({projectDTO: BURole.businessUnit,
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


    function redirectToTeams(){
        project.set(JSON.stringify(BURole));
        goto(`/company/project/teams`);
    }

    function redirectToWhiteboard(){
        project.set(JSON.stringify(BURole));
        goto('/company/project/whiteboard');
    }

</script>

<div class="clickable not-selectable BUwindow">
    {#if BURole.role.name === "MANAGER"}
        <!--the manager stuff here-->

        <span on:click={redirectToTeams}>{BURole.businessUnit.name}</span>
        <img class="clickable not-selectable" src="{whiteboardIcon}" alt="" draggable="false" on:click={redirectToWhiteboard}>
        <img class="clickable not-selectable" src="{editIcon}" alt="" draggable="false" on:click={() => editPopup = true}>
        <img class="clickable not-selectable" src="{leaveIcon}" alt="" draggable="false" on:click={() => leavePopup = true}>
        <img class="clickable not-selectable" src="{deleteIcon}" alt="" draggable="false" on:click={() => deletePopup = true}>

    {:else if BURole.role.name === "EMPLOYEE"}
        <!--the employee stuff here-->
        <span on:click={redirectToTeams}>{BURole.businessUnit.name}</span>
        <img class="clickable not-selectable" src="{whiteboardIcon}" alt="" draggable="false" on:click={redirectToWhiteboard}>
        <img class="clickable not-selectable" src="{leaveIcon}" alt="" draggable="false" on:click={() => leavePopup = true}>
    {/if}
</div>

<Modal bind:open={leavePopup} size="xs" autoclose>
    <div class="text-center">
        <svg aria-hidden="true" class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
        Сигурни ли сте, че искате да напуснете проекта?
        <Button color="red" class="mr-2" on:click={leaveBU}>Да</Button>
        <Button color='alternative'>Не</Button>
    </div>
</Modal>

<Modal bind:open={deletePopup} size="xs" autoclose>
    <div class="text-center">
        <svg aria-hidden="true" class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
        Сигурни ли сте, че искате да изтриете проекта?
        <Button color="red" class="mr-2" on:click={deleteBU}>Да</Button>
        <Button color='alternative'>Не</Button>
    </div>
</Modal>

<Modal title="Редактрине на проекта" bind:open={editPopup} size="XL" autoclose>
    <div class="grid gap-6 mb-6 md:grid-cols-1">
        <div>
            <Label for="companyName" class="mb-2">Име на проекта</Label>
            <Input type="text" id="companyName" required >
                <input type="text" placeholder="{BURole.businessUnit.name}" bind:value={buName} required/>
            </Input>
            <div>
                <img class="clickable not-selectable" src="{inviteToCompanyIcon}" alt="" draggable="false" on:click={() => inviteToProjectPopup = true}>
            </div>
        </div>
        <Button type="submit" on:click={editBU}>Редактиране</Button>
    </div>
</Modal>

<Modal title="Покани хора в {BURole.businessUnit.name}" bind:open={inviteToProjectPopup} size="XL" autoclose>
    <form>
        <div class="grid gap-6 mb-6 md:grid-cols-1">
            <div>
                <Label for="projectName" class="mb-2">Имейл на човека</Label>
                <Input type="text" id="projectName" required>
                    <input type="text" bind:value={inviteeEmail} />
                </Input>
            </div>
            <Button type="submit" on:click={invitePersonToProject}>Изпращане</Button>
        </div>
    </form>
</Modal>

<style lang="scss">
    :root{
        background-color: #F8F8F8;
    }

    .BUwindow{
        background-color: orange;
        width: 97vw;
        display: flex;
        flex-direction: row;
    }

    img {
        width: 50px;
    }

    span {
        text-transform: capitalize;
        font-family: Bahnschrift, monospace;
        background-color: red;
        margin-right: 50vw;
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

    .addBU{
        display: flex;
        flex-direction: row;
        justify-content: flex-end;
        margin-right: 1.5vw;
        margin-top: 1vh;
    }

</style>