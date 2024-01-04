<script>
    import {goto} from "$app/navigation";
    import loadingGif from "$lib/images/loading.gif";
    import BoardComponent from "$lib/components/BoardComponent.svelte";
    import Header from "$lib/components/Header.svelte";
    import {Breadcrumb, BreadcrumbItem, Button, Input, Label, Modal, Textarea} from "flowbite-svelte";
    import plusIcon from "$lib/images/plus.png";
    import {onMount} from "svelte";
    import {userEmail, loggedIn} from "$lib/stores";
    import {getToastStore} from "@skeletonlabs/skeleton";

    export let BURole;
    const toastStore = getToastStore();

    let createPopup = false;

    let columns = [];
    let notes = [];
    let items;

    onMount(() => {
        getColumns(BURole.businessUnit.whiteboard);
    });

    function getColumns(whiteboard) {
        let promise = fetch("http://localhost:8080/company/columns", {
            method: 'POST',
            headers: {
                'Content-Type': "application/json",
            },
            body: JSON.stringify({
                whiteboardDTO: whiteboard,
                businessUnitDTO: BURole.businessUnit
            }),
            credentials: "include"
        }).then(response => {
            if (response.status === 200) {
                return response.json().then(data => {
                    return data;
                });
            } else if (response.status === 204) {
                toastStore.trigger({
                    message: "No columns found!",
                    timeout: 3000,
                    hoverable: true,
                    background: 'bg-yellow-200 rounded-lg border-2 border-yellow-300'
                });
            } else if (response.status === 400) {
                response.text().then(data => {
                    toastStore.trigger({
                        message: "Bad request!",
                        timeout: 3000,
                        hoverable: true,
                        background: 'bg-red-200 rounded-lg border-2 border-red-300'
                    });
                });
            } else if (response.status === 401) {
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if (response.status === 403){
                toastStore.trigger({
                    message: "No permission!",
                    timeout: 3000,
                    hoverable: true,
                    background: 'bg-red-200 rounded-lg border-2 border-red-300'
                });
            } else if (response.status === 500) {
                response.text().then(data => {
                    toastStore.trigger({
                        message: "Something went wrong",
                        timeout: 3000,
                        hoverable: true,
                        background: 'bg-red-200 rounded-lg border-2 border-red-300'
                    });
                });
            }
        }).catch(error => {
            toastStore.trigger({
                message: "Server is offline!",
                timeout: 3000,
                hoverable: true,
                background: 'bg-red-200 rounded-lg border-2 border-red-300'
            });
        });

        promise.then(data => {
            columns = data;
            makeColumns(columns);
        }).catch(error => {
            console.error(error);
        });


    }

    function makeColumns(){
        const promises = [];

        columns.forEach(function (column){
            const promise = getNotes(column);
            promises.push(promise);
        })

        Promise.all(promises)
            .then((allNotesData) => {
                //creates the array
                items = columns.map((column, index) => ({
                    id: column.id,
                    name: column.name,
                    position: column.position,
                    whiteboardDTO: column.whiteboardDTO,
                    items: allNotesData[index] || [],
                }));
                //sorts the array by positions and returns it
                items.sort((a, b) => a.position - b.position);

                // Sort the inner arrays individually based on the position of the first item in each inner array
                items.forEach(item => {
                    item.items.sort((a, b) => a.position - b.position);
                });

                return items;
            });
    }

    function getNotes(column) {
        let promise = fetch("http://localhost:8080/company/whiteboard/notes", {
            method: 'POST',
            headers: {
                'Content-Type': "application/json",
            },
            body: JSON.stringify({
                columnDTO: column,
                businessUnitDTO: BURole.businessUnit
            }),
            credentials: "include"
        }).then(response => {
            if (response.status === 200) {
                return response.json();
            } else if (response.status === 400) {
                response.text().then(data => {
                    toastStore.trigger({
                        message: "Bad request!",
                        timeout: 3000,
                        hoverable: true,
                        background: 'bg-red-200 rounded-lg border-2 border-red-300'
                    });
                });
            } else if (response.status === 401) {
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if (response.status === 403){
                toastStore.trigger({
                    message: "No permission!",
                    timeout: 3000,
                    hoverable: true,
                    background: 'bg-red-200 rounded-lg border-2 border-red-300'
                });
            } else if (response.status === 500) {
                response.text().then(data => {
                    toastStore.trigger({
                        message: "Something went wrong",
                        timeout: 3000,
                        hoverable: true,
                        background: 'bg-red-200 rounded-lg border-2 border-red-300'
                    });
                });
            }
        }).catch(error => {
            toastStore.trigger({
                message: "Server is offline!",
                timeout: 3000,
                hoverable: true,
                background: 'bg-red-200 rounded-lg border-2 border-red-300'
            });
        });


        return promise.then(data => {
            notes = data;
            return notes;
        }).catch(error => {
            console.error(error);
        });

    }

    //I know this makes more than necessary requests but ehhh
    //no time to optimize now
    function handleBoardUpdated(newItems){
        updatePlace(newItems);
        items = newItems;
    }

    function updatePlace(newItems){
        let updatedNotes = [];
        for (let i = 0; i < newItems.length; i++) {
            const newItem = newItems[i];
            for (let j = 0; j < newItem.items.length; j++) {
                const note = newItem.items[j];
                note.columnDTO = {
                    id: newItem.id,
                    name: newItem.name,
                    whiteboardDTO: newItem.whiteboardDTO,
                    position: newItem.position
                };
                note.position = j;

                updatedNotes.push(note);
            }
        }

        updateNotes(updatedNotes);
    }

    //TODO: Update the place after 200ok
    function updateNotes(updatedNotes){
        fetch("http://localhost:8080/company/whiteboard/updateNotes", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                notes: updatedNotes,
                businessUnitDTO: BURole.businessUnit
            }),
            credentials: "include"
        }).then(response=>{
            if (response.status === 200) {
                //nothing I guess
            } else if(response.status === 400){
                response.text().then(data => {
                    toastStore.trigger({
                        message: "Bad request!",
                        timeout: 3000,
                        hoverable: true,
                        background: 'bg-red-200 rounded-lg border-2 border-red-300'
                    });
                });
            } else if(response.status === 401){
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if (response.status === 403){
                toastStore.trigger({
                    message: "No permission!",
                    timeout: 3000,
                    hoverable: true,
                    background: 'bg-red-200 rounded-lg border-2 border-red-300'
                });
            } else if(response.status === 500){
                response.text().then(data => {
                    toastStore.trigger({
                        message: "Something went wrong",
                        timeout: 3000,
                        hoverable: true,
                        background: 'bg-red-200 rounded-lg border-2 border-red-300'
                    });
                });
            }
        }).catch(error => {
            toastStore.trigger({
                message: "Server is offline!",
                timeout: 3000,
                hoverable: true,
                background: 'bg-red-200 rounded-lg border-2 border-red-300'
            });
        });
    }

    let noteName;
    let noteDescription;

    function createNote(){
        const newNote = {
            id: null,
            name: noteName,
            description: noteDescription,
            columnDTO: {
                id: items[0].id,
                name: items[0].name,
                whiteboardDTO: items[0].whiteboardDTO,
                position: items[0].position
            },
            position: (items[0].items.length > 0) ? items[0].items.length : 0
        };

        createNoteRequest(newNote);
    }

    function createNoteRequest(note){
        fetch("http://localhost:8080/company/whiteboard/createNote", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                noteDTO: note,
                businessUnitDTO: BURole.businessUnit
            }),
            credentials: "include"
        }).then(response=>{
            if (response.status === 201) {
                items = [{
                    id: items[0].id,
                    name: items[0].name,
                    items: items[0].items.concat(note)
                },
                    ...items.slice(1) // include the rest of the items array
                ];
                noteName = "";
                noteDescription = "";
                createPopup = false;
                makeColumns();
            } else if(response.status === 400){
                response.text().then(data => {
                    toastStore.trigger({
                        message: "Bad request!",
                        timeout: 3000,
                        hoverable: true,
                        background: 'bg-red-200 rounded-lg border-2 border-red-300'
                    });
                });
            } else if(response.status === 401){
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if (response.status === 403){
                toastStore.trigger({
                    message: "No permission!",
                    timeout: 3000,
                    hoverable: true,
                    background: 'bg-red-200 rounded-lg border-2 border-red-300'
                });
            } else if(response.status === 500){
                response.text().then(data => {
                    toastStore.trigger({
                        message: "Something went wrong",
                        timeout: 3000,
                        hoverable: true,
                        background: 'bg-red-200 rounded-lg border-2 border-red-300'
                    });
                });
            }
        }).catch(error => {
            toastStore.trigger({
                message: "Server is offline!",
                timeout: 3000,
                hoverable: true,
                background: 'bg-red-200 rounded-lg border-2 border-red-300'
            });
        });
    }

</script>
{#await items}
    <img src="{loadingGif}" alt="">
{:then items}
    <Header />
    <div class="lowerMenuDiv">
        <Breadcrumb>
            {#if BURole.businessUnit.type === "COMPANY"}
                <BreadcrumbItem href="/companies" home>{BURole.businessUnit.name}</BreadcrumbItem>
            {:else if BURole.businessUnit.type === "PROJECT" || BURole.businessUnit.type === "TEAM"}
                <BreadcrumbItem href="/companies" home>{BURole.businessUnit.company.name}</BreadcrumbItem>
            {/if}
            {#if BURole.businessUnit.type === "PROJECT"}
                <BreadcrumbItem href="/company/projects">{BURole.businessUnit.name}</BreadcrumbItem>
            {:else if BURole.businessUnit.type === "TEAM"}
                <BreadcrumbItem href="/company/projects">{BURole.businessUnit.project.name}</BreadcrumbItem>
            {/if}
            {#if BURole.businessUnit.type === "TEAM"}
                <BreadcrumbItem href="/company/project/teams">{BURole.businessUnit.name}</BreadcrumbItem>
            {/if}
        </Breadcrumb>
    </div>

    {#if items}
        <div class="row">
            <div class="addNote clickable" on:click={() => createPopup = true}>
                <img class="clickable not-selectable noteAdd" src="{plusIcon}" alt="" draggable="false" />
            </div>
            <BoardComponent columns="{items}" onFinalUpdate={handleBoardUpdated} BURole="{BURole}"/>
        </div>
    {/if}
{/await}


<Modal title="Create note" bind:open={createPopup} size="xs" outsideclose>
    <form>
        <div class="grid gap-6 mb-6 md:grid-cols-1">
            <div>
                <Label for="noteName" class="mb-2">Name</Label>
                <Input type="text" id="noteName" bind:value={noteName} required/>
                <Label for="noteDescription" class="mb-2">Description</Label>
                <Textarea id="noteDescription" rows="3" bind:value={noteDescription}/>
            </div>
            <Button type="submit" on:click={createNote} color="blue">Create</Button>
        </div>
    </form>
</Modal>

<style lang="scss">

    .lowerMenuDiv{
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        margin-top: 1vh;
        margin-left: 1vw;
    }

    .row {
        display: flex;
        flex-direction: row;
    }

    .addNote{
        margin: 20px 5vh 0 2vh;
        max-height: 85vh;
        width: 3vw;
        display: flex;
        justify-content: center;
        align-items: center;
        border: 2px solid #BBBBBB;
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

    img{
        width: 30px;
    }

</style>