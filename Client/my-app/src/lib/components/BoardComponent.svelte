<script>
    import { flip } from 'svelte/animate';
    import ColumnComponent from "$lib/components/ColumnComponent.svelte";

    const flipDurationMs = 300;

    //Gets columns from parent (together with the notes inside)
    export let columns;

    //updates the parent so he know when an update happens
    export let onFinalUpdate;

    //For moving the columns around
    function handleDndConsiderColumns(e) {
        columns = e.detail.items;
    }

    //For moving the columns around
    function handleDndFinalizeColumns(e) {
        onFinalUpdate(e.detail.items);
    }

    function handleItemFinalize(columnIdx, newItems) {
        columns[columnIdx].items = newItems;
        onFinalUpdate([...columns]);
    }

</script>

{#await columns}

{:then columns}
    <section class="board" >
        {#each columns as {id, name, position, items}, idx (id)}
            <div class="column" animate:flip="{{duration: flipDurationMs}}" >
                <ColumnComponent name={name} items={items} onDrop={(newItems) => handleItemFinalize(idx, newItems)} />
            </div>
        {/each}
    </section>
{/await}



<style>
    .board {
        height: 85vh;
        width: 100%;

        padding: 0.5em;
        margin-left: 2.5vw;

        display: flex;
        flex-wrap: wrap;
        max-width: 95vw;



        background-color: red;
    }
    .column {
        background-color: orange;
        height: 100%;
        padding: 0.5em;
        margin: 10px;

        flex-grow: 2;

        border: 1px solid #333333;
        max-width: 25%;
        overflow-x: hidden;
        justify-content: space-between;
    }

</style>