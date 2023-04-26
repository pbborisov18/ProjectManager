<script>
    import { flip } from 'svelte/animate';
    import { dndzone } from 'svelte-dnd-action';
    import {Button, Card, Input, Label, Modal, Textarea, CloseButton} from "flowbite-svelte";
    import inviteToCompanyIcon from "$lib/images/invitation.png";
    import deleteIcon from "$lib/images/delete.png"
    import {company, project, team} from "$lib/stores.js";
    import {goto} from "$app/navigation";

    let companyObj = JSON.parse($company);
    let projectObj = JSON.parse($project);
    let teamObj = JSON.parse($team);

    let currentUrl = window.location.pathname;

    const flipDurationMs = 150;

    //column name from parent
    export let name;
    //get stuff from parent
    export let items;

    export let onDrop;

    function handleDndConsiderCards(e) {
        items = e.detail.items;
    }

    function handleDndFinalizeCards(e) {
        onDrop(e.detail.items);
    }

    let deletePopup = false;
    let editPopup = false;

    let clickedNote;
    let noteName;
    let noteDescription;

    function editNote(){
        if(clickedNote){
            clickedNote = {...clickedNote, name: noteName, description: noteDescription};

            let fetchUrl;
            let bu;

            if(currentUrl === "/company/whiteboard"){
                fetchUrl = "http://localhost:8080/company/whiteboard/updateNote"
                bu = companyObj;
            } else if(currentUrl === "/company/project/whiteboard"){
                fetchUrl = "http://localhost:8080/company/project/whiteboard/updateNote"
                bu = projectObj;
            } else if(currentUrl === "/company/project/team/whiteboard"){
                fetchUrl = "http://localhost:8080/company/project/team/whiteboard/updateNote"
                bu = teamObj;
            }

            fetch(fetchUrl, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({noteDTO: clickedNote,
                    businessUnitDTO: bu.businessUnit}),
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

            items = items.map(item => {
                if (item.id === clickedNote.id) {
                    return clickedNote;
                } else {
                    return item;
                }
            });

        }

        //do the update fetch here
        //then call onDrop with the updated shit
        //or straight up update it
    }

    function deleteNote(){
        if(clickedNote) {
            clickedNote = {...clickedNote, name: noteName, description: noteDescription};

            let fetchUrl;
            let bu;

            if (currentUrl === "/company/whiteboard") {
                fetchUrl = "http://localhost:8080/company/whiteboard/deleteNote"
                bu = companyObj;
            } else if (currentUrl === "/company/project/whiteboard") {
                fetchUrl = "http://localhost:8080/company/project/whiteboard/deleteNote"
                bu = projectObj;
            } else if (currentUrl === "/company/project/team/whiteboard") {
                fetchUrl = "http://localhost:8080/company/project/team/whiteboard/deleteNote"
                bu = teamObj;
            }

            fetch(fetchUrl, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({noteDTO: clickedNote,
                    businessUnitDTO: bu.businessUnit}),
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

            items = items.filter(item => item.id !== clickedNote.id);
        }
    }

    let hoveredItem = null;
    let hoverTimer = null;

    function handleHover(item) {
        hoverTimer = setTimeout(() => {
            hoveredItem = item;
        }, 2000);
    }

    function handleHoverEnd(item) {
        if (hoverTimer) {
            clearTimeout(hoverTimer);
            hoverTimer = null;
            hoveredItem = null;
        }
    }



</script>

<div class='wrapper'>
    <div class="column-title">
        {name}
    </div>
    <div class="column-content" use:dndzone={{items, flipDurationMs, zoneTabIndex: -1}}
         on:consider={handleDndConsiderCards}
         on:finalize={handleDndFinalizeCards}>

        {#each items as item (item.id)}
            <div animate:flip="{{duration: flipDurationMs}}"
                 on:dblclick={() => {
                    clickedNote = item;
                    noteName = item.name;
                    noteDescription = item.description;
                    editPopup = true;
                 }}
                 on:mouseenter={handleHover(item)} on:mouseleave={handleHoverEnd}>
                <Card>
                    <div class="parent">
                        <h5 class="card mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white">{item.name}</h5>
                        {#if hoveredItem === item}
                            <CloseButton class="close-button" on:click={() => {
                                clickedNote = item;
                                deletePopup = true;
                            }}/>
                        {/if}
                    </div>
                        <hr>
                        <p class="card font-normal text-gray-700 dark:text-gray-400 leading-tight">{item.description}</p>

                </Card>
            </div>
        {/each}

    </div>
</div>

<Modal title="Редактрине на бележка" bind:open={editPopup} size="XL" autoclose>
    <div class="grid gap-6 mb-6 md:grid-cols-1">
        <div>
            <Label for="noteName" class="mb-2">Име</Label>
            <Input type="text" id="noteName" required>
                <input type="text" bind:value={noteName}/>
            </Input>
            <Label for="noteDescription" class="mb-2">Съдържание</Label>
            <Textarea id="noteDescription" rows="3" bind:value={noteDescription}/>
        </div>
        <Button type="submit" on:click={editNote}>Редактиране</Button>
    </div>
</Modal>

<Modal bind:open={deletePopup} size="xs" autoclose>
    <div class="text-center">
        <svg aria-hidden="true" class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
        Сигурни ли сте, че искате да изтриете бележката?
        <Button color="red" class="mr-2" on:click={deleteNote}>Да</Button>
        <Button color='alternative'>Не</Button>
    </div>
</Modal>

<style>

    .wrapper {
        height: 100%;
        width: 100%;
        /*Notice we make sure this container doesn't scroll so that the title stays on top and the dndzone inside is scrollable*/
        overflow-y: hidden;
    }

    .column-content {
        height: calc(100% - 2.5em);
        /* Notice that the scroll container needs to be the dndzone if you want dragging near the edge to trigger scrolling */
        overflow-y: auto;
    }

    .column-title {
        height: 2.5em;
        font-weight: bold;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .card{
        overflow: hidden;
        overflow-y: auto;
        overflow-wrap: anywhere;
        max-height: 20vh;
        line-height: normal;
    }

    img{
        height: 50px;
        width: 50px;
    }

    .close-button{
        position: absolute;
        top: 0;
        right: 0;
        z-index: 1;
    }

    .parent{
        display: flex;
        flex-direction: row;
    }

</style>