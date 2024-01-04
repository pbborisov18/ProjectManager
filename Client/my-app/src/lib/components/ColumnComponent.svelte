<script>
    import { flip } from 'svelte/animate';
    import { dndzone } from 'svelte-dnd-action';
    import {Button, Card, Input, Label, Modal, Textarea, CloseButton} from "flowbite-svelte";
    import {goto} from "$app/navigation";
    import {userEmail, loggedIn} from "$lib/stores";
    import toast from "svelte-french-toast";

    export let BURole;

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

    //TODO: Fix up popups get autoclosed only on success
    let deletePopup = false;
    let editPopup = false;

    let clickedNote;
    let noteName;
    let noteDescription;

    function editNote(){
        if(!clickedNote) {
            return;
        }

        clickedNote = {...clickedNote, name: noteName, description: noteDescription};

        fetch("http://localhost:8080/company/whiteboard/updateNote", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                noteDTO: clickedNote,
                businessUnitDTO: BURole.businessUnit
            }),
            credentials: "include"
        }).then(response=>{
            if (response.status === 200) {
                toast.success("Saved");
            } else if(response.status === 400){
                response.text().then(data => {
                    toast.error("Bad request!");
                });
            } else if(response.status === 401){
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 403){
                toast.error("No permission!");
            } else if(response.status === 500){
                response.text().then(data => {
                    toast.error("Something went wrong");
                });
            }
        }).catch(error => {
            toast.error("Server is offline!");
        });

        items = items.map(item => {
            if (item.id === clickedNote.id) {
                return clickedNote;
            } else {
                return item;
            }
        });

    }

    function deleteNote(){
        if(!clickedNote) {
            return;
        }

        fetch("http://localhost:8080/company/whiteboard/deleteNote", {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                noteDTO: clickedNote,
                businessUnitDTO: BURole.businessUnit
            }),
            credentials: "include"
        }).then(response=>{
            if (response.status === 200) {
                items = items.filter(item => item.id !== clickedNote.id);
            } else if(response.status === 400){
                response.text().then(data => {
                    toast.error("Bad request!");
                });
            } else if(response.status === 401){
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 403){
                toast.error("No permission!");
            } else if(response.status === 500){
                response.text().then(data => {
                    toast.error("Something went wrong");
                });
            }
        }).catch(error => {
            toast.error("Server is offline!");
        });
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

<Modal title="Edit note" bind:open={editPopup} size="xs" autoclose>
    <div class="grid gap-6 mb-6 md:grid-cols-1">
        <div>
            <Label for="noteName" class="mb-2">Name</Label>
            <Input type="text" id="noteName" required bind:value={noteName}/>
            <Label for="noteDescription" class="mb-2">Description</Label>
            <Textarea id="noteDescription" rows="3" bind:value={noteDescription}/>
        </div>
        <Button type="submit" on:click={editNote} color="green">Edit</Button>
    </div>
</Modal>

<Modal bind:open={deletePopup} size="xs" autoclose outsideclose>
        <div class="text-center">
            <svg aria-hidden="true" class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
            <h3 class="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">Are you sure you want to delete the note?</h3>
            <Button color="alternative" class="me-2">No</Button>
            <Button color="red" on:click={deleteNote}>Yes</Button>
        </div>
</Modal>

<style>

    .wrapper {
        height: 100%;
        width: 100%;
        overflow-y: hidden;
    }

    .column-content {
        height: calc(100% - 2.5em);
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