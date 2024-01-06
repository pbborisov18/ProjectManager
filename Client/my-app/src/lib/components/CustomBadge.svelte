<script>

    import {CloseButton} from "flowbite-svelte";

    export let onClick;
    export let onDestroy;
    export let toDisplay;
    export let deleteButton;

</script>


<div class="mainDiv">
    <div class="textDiv not-selectable clickable" on:click={onClick}>
        {toDisplay}
    </div>
<!--    Yes this is stupid I know-->
    {#if !deleteButton}
        <CloseButton class="" on:click={onDestroy}/>
    {/if}
</div>



<style lang="scss">

    .mainDiv {
        display: flex;
        border-radius: 15px;
        border: 2px solid black;
        //background-color: green;

        .textDiv {
            flex-grow: 1;
            color: black;
            text-align: center;
            padding: 1vh;
            margin-right: 2px;
            border-radius: 15px 0 0 15px;
            //background-color: blue;
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
</style>