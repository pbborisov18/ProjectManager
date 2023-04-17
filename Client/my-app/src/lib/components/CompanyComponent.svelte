<script>

    import {goto} from "$app/navigation";
    import {Button, Input, Label, Modal} from 'flowbite-svelte'
    import "../../tailwind.css";
    import { company } from "$lib/stores.js";
    import whiteboardIcon from "$lib/images/rectangle.png";
    import leaveIcon from "$lib/images/leave.png";
    import deleteIcon from "$lib/images/delete.png";
    import editIcon from "$lib/images/edit.png";
    import inviteToCompanyIcon from "$lib/images/invitation.png";

    let value;

    let leavePopup = false;
    let deletePopup = false;
    let editPopup = false;
    let inviteToCompanyPopup = false;

    export let onDestroy;
    export let BURole;


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
        if(!value){
            alert("Полето не може да е празно!");
        }else {
            let updatedBURole = {
                ...BURole.businessUnit,
                name: value
            };
            fetch('http://localhost:8080/updateCompany', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedBURole),
                credentials: "include"
            }).then(response=>{
                if (response.status === 201) {
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
            body: JSON.stringify({companyDTO: BURole.businessUnit,
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

    function redirectToProjects(){
        company.set(JSON.stringify(BURole));
        goto(`/company/projects`);
    }

    function redirectToWhiteboard(){
        company.set(JSON.stringify(BURole));
        goto('/company/whiteboard');
    }

</script>

<div class="clickable not-selectable BUwindow">
    {#if BURole.role.name === "MANAGER"}
        <!--the manager stuff here-->

        <span on:click={redirectToProjects}>{BURole.businessUnit.name}</span>
        <div class="imageDivs">
            <img class="clickable not-selectable" src="{whiteboardIcon}" alt="" draggable="false" on:click={redirectToWhiteboard}>
        </div>
        <div class="imageDivs">
            <img class="clickable not-selectable" src="{editIcon}" alt="" draggable="false" on:click={() => editPopup = true}>
        </div>
        <div class="imageDivs">
            <img class="clickable not-selectable" src="{leaveIcon}" alt="" draggable="false" on:click={() => leavePopup = true}>
        </div>
        <div class="imageDivs">
            <img class="clickable not-selectable" src="{deleteIcon}" alt="" draggable="false" on:click={() => deletePopup = true}>
        </div>
    {:else if BURole.role.name === "EMPLOYEE"}
        <!--the employee stuff here-->
        <span on:click={redirectToProjects}>{BURole.businessUnit.name}</span>
        <div class="imageDivs">
        <img class="clickable not-selectable" src="{whiteboardIcon}" alt="" draggable="false" on:click={redirectToWhiteboard}>
        </div>
        <div class="imageDivs">
            <img class="clickable not-selectable" src="{leaveIcon}" alt="" draggable="false" on:click={() => leavePopup = true}>
        </div>
    {/if}
</div>

<Modal bind:open={leavePopup} size="xs" autoclose>
    <div class="text-center">
        <svg aria-hidden="true" class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
        Сигурни ли сте, че искате да напуснете компанията?
        <Button color="red" class="mr-2" on:click={leaveBU}>Да</Button>
        <Button color='alternative'>Не</Button>
    </div>
</Modal>

<Modal bind:open={deletePopup} size="xs" autoclose>
    <div class="text-center">
        <svg aria-hidden="true" class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
        Сигурни ли сте, че искате да изтриете компанията?
        <Button color="red" class="mr-2" on:click={deleteBU}>Да</Button>
        <Button color='alternative'>Не</Button>
    </div>
</Modal>

<Modal title="Редактрине на компанията" bind:open={editPopup} size="XL" autoclose>
    <div class="grid gap-6 mb-6 md:grid-cols-1">
        <div>
            <Label for="companyName" class="mb-2">Име на компанията</Label>
            <Input type="text" id="companyName" required >
                <input type="text" placeholder="{BURole.businessUnit.name}" bind:value required/>
            </Input>
            <div>
                <img class="clickable not-selectable" src="{inviteToCompanyIcon}" alt="" draggable="false" on:click={() => inviteToCompanyPopup = true}>
            </div>
        </div>
        <Button type="submit" on:click={editBU}>Редактиране</Button>
    </div>
</Modal>

<Modal title="Покани хора в {BURole.businessUnit.name}" bind:open={inviteToCompanyPopup} size="XL" autoclose>
    <form>
        <div class="grid gap-6 mb-6 md:grid-cols-1">
            <div>
                <Label for="projectName" class="mb-2">Имейл на човека</Label>
                <Input type="text" id="projectName" required>
                    <input type="text" bind:value={inviteeEmail} />
                </Input>
            </div>
            <Button type="submit" on:click={invitePersonToCompany}>Изпращане</Button>
        </div>
    </form>
</Modal>

<style lang="scss">
  :root{
    background-color: #F8F8F8;
  }


  .BUwindow {
      background-color: rgba(104, 153, 168, 0.99);
      margin-top: 1vh;
      width: 97vw;
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      align-items: center; /* align items vertically */
      border: 0 solid #5A4A42;
      box-shadow: 0px 0px 1px 1px rgba(139, 224, 250, 0.99);


      span {
          flex-basis: 65%;
          flex-grow: 1;
          white-space: nowrap; /* prevent text from wrapping */
          overflow: hidden; /* hide overflow */
          text-overflow: ellipsis; /* show ellipsis for truncated text */
          font-family: Bahnschrift, monospace;
          //background-color: red;
          height: 100%;
          vertical-align: middle;
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
          max-width: 50px;
          max-height: 50px;
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

  //.addBU{
  //    display: flex;
  //    flex-direction: row;
  //    justify-content: flex-end;
  //    margin-right: 1.5vw;
  //    margin-top: 1vh;
  //}

</style>