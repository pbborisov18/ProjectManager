<script>

    import {goto} from "$app/navigation";
    import {Button, Input, Label, Modal} from 'flowbite-svelte'
    import "../../tailwind.css";
    import { company } from "$lib/stores.js";
    import whiteboardIcon from "$lib/images/rectangle.png";
    import leaveIcon from "$lib/images/leave.png";
    import deleteIcon from "$lib/images/delete.png";
    import editIcon from "$lib/images/edit.png";
    import inviteToCompanyIcon from "$lib/images/invite.png";



    let leavePopup = false;
    let deletePopup = false;
    let editPopup = false;
    let inviteToCompanyPopup = false;

    export let onDestroy;
    export let BURole;

    let BUEditName = BURole.businessUnit.name;

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
        if(!BUEditName){
            alert("Полето не може да е празно!");
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

        <div style="border-left:1px solid #BBBBBB;height:80%"></div>
        <div class="imageDivs">
            <img class="clickable not-selectable" src="{whiteboardIcon}" alt="" draggable="false" on:click={redirectToWhiteboard}>
        </div>
        <div style="border-left:1px solid #BBBBBB;height:80%"></div>
        <div class="imageDivs">
            <img class="clickable not-selectable" src="{editIcon}" alt="" draggable="false" on:click={() => editPopup = true}>
        </div>
        <div style="border-left:1px solid #BBBBBB;height:80%"></div>
        <div class="imageDivs">
            <img class="clickable not-selectable" src="{leaveIcon}" alt="" draggable="false" on:click={() => leavePopup = true}>
        </div>
        <div style="border-left:1px solid #BBBBBB;height:80%"></div>
        <div class="imageDivs">
            <img class="clickable not-selectable xImage" src="{deleteIcon}" alt="" draggable="false" on:click={() => deletePopup = true}>
        </div>
    {:else if BURole.role.name === "EMPLOYEE"}
        <!--the employee stuff here-->
        <span on:click={redirectToProjects}>{BURole.businessUnit.name}</span>
        <div style="border-left:1px solid #BBBBBB;height:80%"></div>
        <div class="imageDivs">
        <img class="clickable not-selectable" src="{whiteboardIcon}" alt="" draggable="false" on:click={redirectToWhiteboard}>
        </div>
        <div style="border-left:1px solid #BBBBBB;height:80%"></div>
        <div class="imageDivs">
            <img class="clickable not-selectable xImage" src="{leaveIcon}" alt="" draggable="false" on:click={() => leavePopup = true}>
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

<Modal title="Редактиране на компанията" bind:open={editPopup} size="XL" autoclose>
        <div class="bodyPopup">

        <div class="editDiv">
            <div class="companyNameLabel">
                <Label for="companyName" class="mb-2">Име на компанията</Label>
                <Input type="text" id="companyName" required >
                    <input class="text-black inputName" type="text" bind:value={BUEditName} required/>
                </Input>
            </div>

            <img class="inviteImg clickable not-selectable" src="{inviteToCompanyIcon}" alt="" draggable="false" on:click={() => inviteToCompanyPopup = true}>

        </div>
        <div>
            <Button type="submit" on:click={editBU}>Редактиране</Button>
        </div>
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
          //max-height: 100%;
          height: 100%;
          display: flex;
          justify-content: center;
          align-items: center;
          //background-color: #e7e7e7;
          //background-color: red;
          //border: 1px solid black;
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

  .companyNameLabel{
      display: flex;
        flex-direction: column
  }

</style>