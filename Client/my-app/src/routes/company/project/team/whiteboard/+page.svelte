
<script>
    import {goto} from "$app/navigation";
    import loadingGif from "$lib/images/loading.gif";
    import BoardComponent from "$lib/components/BoardComponent.svelte";
    import Header from "$lib/components/Header.svelte";
    import {Breadcrumb, BreadcrumbItem, Button, Input, Label, Modal, Textarea} from "flowbite-svelte";
    import {company, project, team} from "$lib/stores.js";
    import plusIcon from "$lib/images/plus.png";

    let companyObj = JSON.parse($company);
    let projectObj = JSON.parse($project);
    let teamObj = JSON.parse($team);

    let createPopup = false;

    let currentUrl = window.location.pathname;


    export let data;
    let columns = [];
    let notes = [];
    let items;

    if(data.whiteboard){
        getColumns(data.whiteboard);
        //this shit doesn't wait for the function to end execution. js
        //make sure to tell the ui to wait for stuff to execute
    } else {
        if(currentUrl === "/company/whiteboard"){
            goto("/company/createWhiteboard");
        } else if(currentUrl === "/company/project/whiteboard"){
            goto("/company/project/createWhiteboard");
        } else if(currentUrl === "/company/project/team/whiteboard"){
            goto("/company/project/team/createWhiteboard")
        }
    }

    function getColumns(whiteboard) {


        let fetchUrl;

        if(currentUrl === "/company/whiteboard"){
            fetchUrl = "http://localhost:8080/company/columns"
        } else if(currentUrl === "/company/project/whiteboard"){
            fetchUrl = "http://localhost:8080/company/project/columns"
        } else if(currentUrl === "/company/project/team/whiteboard"){
            fetchUrl = "http://localhost:8080/company/project/team/columns"
        }

        let promise = fetch(fetchUrl, {
            method: 'POST',
            headers: {
                'Content-Type': "application/json",
            },
            body: JSON.stringify(whiteboard),
            credentials: "include"
        }).then(response => {
            if (response.status === 200) {
                return response.json().then(data => {
                    return data;
                });
            } else if (response.status === 204) {
                //no columns lol. wtf are you doing boi
            } else if (response.status === 400) {
                response.text().then(text => {
                    throw new Error(text);
                })
            } else if (response.status === 401) {
                response.text().then(text => {
                    throw new Error(text);
                });
                goto("/login");
            } else if (response.status === 500) {
                response.text().then(text => {
                    throw new Error(text);
                });
            }

        }).catch(error => {
            console.error(error);
        });

        promise.then(data => {
            columns = data;
            makeColumns(columns);
        }).catch(error => {
            console.error(error);
        });


    }

    function getNotes(column) {

        let fetchUrl;

        if(currentUrl === "/company/whiteboard"){
            fetchUrl = "http://localhost:8080/company/whiteboard/notes"
        } else if(currentUrl === "/company/project/whiteboard"){
            fetchUrl = "http://localhost:8080/company/project/whiteboard/notes"
        } else if(currentUrl === "/company/project/team/whiteboard"){
            fetchUrl = "http://localhost:8080/company/project/team/whiteboard/notes"
        }

        let promise = fetch(fetchUrl, {
            method: 'POST',
            headers: {
                'Content-Type': "application/json",
            },
            body: JSON.stringify(column),
            credentials: "include"
        }).then(response => {
            if (response.status === 200) {
                return response.json();
            } else if (response.status === 400) {
                response.text().then(text => {
                    throw new Error(text);
                })
            } else if (response.status === 401) {
                response.text().then(text => {
                    throw new Error(text);
                });
                goto("/login");
            } else if (response.status === 500) {
                response.text().then(text => {
                    throw new Error(text);
                });
            }

        }).catch(error => {
            console.error(error);
        });


        return promise.then(data => {
            notes = data;
            return notes;
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

        const promises2 = columns.map(column => {
            const promise = getNotes(column);
            return promise || []; // Replace undefined with empty array
        });

        Promise.all(promises)
            .then((notesData) => {
                //creates the array
                items = columns.map((column, index) => ({
                    id: column.id,
                    name: column.name,
                    position: column.position,
                    whiteboardDTO: column.whiteboardDTO,
                    items: notesData[index] || [],
                }));
                //sorts the array by positions and returns it
                return items.sort((a, b) => a.position - b.position);
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
                note.columnDTO = {id: newItem.id,
                    name: newItem.name,
                    whiteboardDTO: newItem.whiteboardDTO,
                    position: newItem.position};
                updatedNotes.push(note);
            }
        }

        updateNotes(updatedNotes);
    }

    function updateNotes(updatedNotes){
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

        updatedNotes.forEach((note) => fetch(fetchUrl, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({noteDTO: note,
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
        }));
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
            }};

        createNoteRequest(newNote);

        items = [{
            id: items[0].id,
            name: items[0].name,
            items: items[0].items.concat(newNote)
        },
            ...items.slice(1) // include the rest of the items array
        ];
    }

    function createNoteRequest(note){
        let fetchUrl;
        let bu;

        if(currentUrl === "/company/whiteboard"){
            fetchUrl = "http://localhost:8080/company/whiteboard/createNote"
            bu = companyObj;
        } else if(currentUrl === "/company/project/whiteboard"){
            fetchUrl = "http://localhost:8080/company/project/whiteboard/createNote"
            bu = projectObj;
        } else if(currentUrl === "/company/project/team/whiteboard"){
            fetchUrl = "http://localhost:8080/company/project/team/whiteboard/createNote"
            bu = teamObj;
        }

        fetch(fetchUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({noteDTO: note,
                businessUnitDTO: bu.businessUnit}),
            credentials: "include"
        }).then(response=>{
            if (response.status === 201) {
                makeColumns();
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

</script>

{#await items}
    <img src="{loadingGif}" alt="">
{:then items}
    <Header />
    <div class="lowerMenuDiv">
        <Breadcrumb >
            <BreadcrumbItem href="/companies" home>{companyObj.businessUnit.name}</BreadcrumbItem>
            <BreadcrumbItem href="/company/projects">{projectObj.businessUnit.name}</BreadcrumbItem>
            <BreadcrumbItem href="/company/project/teams">{teamObj.businessUnit.name}</BreadcrumbItem>
        </Breadcrumb>
    </div>
    {#if items}
        <div class="row">
            <div class="addNote clickable" on:click={() => createPopup = true}>
                <img class="clickable not-selectable" src="{plusIcon}" alt="" draggable="false" />
            </div>
            <BoardComponent columns="{items}" onFinalUpdate={handleBoardUpdated}/>
        </div>
    {/if}
{/await}



<Modal title="Създаване на бележка" bind:open={createPopup} size="xl" autoclose>
    <form>
        <div class="grid gap-6 mb-6 md:grid-cols-1">
            <div>
                <Label for="noteName" class="mb-2">Име</Label>
                <Input type="text" id="noteName" required>
                    <input type="text" bind:value={noteName} />
                </Input>
                <Label for="noteDescription" class="mb-2">Съдържание</Label>
                <Textarea id="noteDescription" rows="3" bind:value={noteDescription}/>
            </div>
            <Button type="submit" on:click={createNote}>Създаване</Button>
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

    .clickable {
        cursor: pointer;
    }

    .not-selectable {
        -webkit-user-select: none; /* Chrome, Safari, Opera */
        -moz-user-select: none; /* Firefox */
        -ms-user-select: none; /* IE 10+ */
        user-select: none; /* Standard syntax */
    }

    .addProject{
        justify-content: flex-end;
        margin-right: 1.5vw;
        margin-top: 1vh;
    }

    .row {
        display: flex;
        flex-direction: row;
    }

    .addNote{
        margin: 20px 5vh 0px 2vh;
        max-height: 85vh;
        width: 3vw;
        display: flex;
        justify-content: center;
        align-items: center;
        border: 2px solid #BBBBBB;
    }

    img{
        width: 30px;
    }
</style>