<script>
    import Header from "$lib/components/Header.svelte";
    import "../tailwind.css";
    import {goto} from "$app/navigation";
    import mainImage from "$lib/images/mainImage.png";
    import {Button} from "flowbite-svelte";

    function redirectToRegister(){
        goto("/sign-up");
    }

</script>


<Header homePage="true"/>

<div class="flex h-[90vh]">
    <div class="flex-1 flex items-center">
        <div class="ml-64  max-w-lg">
            <h2 class="font-semibold text-[2.5em] text-blueGray-600">
                PROJECT MANAGER
            </h2>
            <p class="mt-4 text-[1.5em] leading-relaxed text-blueGray-500">
                Keep your projects organized, on schedule, and in one place with our easy-to-use platform.
            </p>
                <Button on:click={redirectToRegister} class="mt-5 bg-red-400 text-white font-bold rounded-md p-[1rem]" color="" >TRY TODAY</Button>
        </div>
    </div>
    <div class="flex-1 mt-[15vh] mr-20">
        <img class="" src="{mainImage}" alt="..."/>
    </div>
</div>

<div class="min-w-fit flex justify-center">
    <p class="ml-5"><b>DISCLAIMER:</b> The frontend has been mainly made for 1080p resolution. More details and a "How to use" guide -> </p>
    <a target="_blank" href="https://github.com/pbborisov18/ProjectManager#demo" class="underline text-blue-600">Github</a>
</div>

<style>

</style>