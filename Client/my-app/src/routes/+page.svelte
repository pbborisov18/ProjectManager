<script>
    import Header from "$lib/components/Header.svelte";
    import "../tailwind.css";
    import {goto} from "$app/navigation";
    const patternVue = "pattern_svelte.png";

    function redirectToLogin(){
        goto("/sign-up");
    }

</script>

<Header homePage="true"/>

<section class="header relative pt-16 items-center flex h-screen max-h-860-px">
  <div class="container mx-auto items-center flex flex-wrap">
    <div class="w-full md:w-8/12 lg:w-6/12 xl:w-6/12 px-4">
      <div class="pt-32 sm:pt-0">
        <h2 class="font-semibold text-4xl text-blueGray-600">
          Agile Ace
        </h2>
        <p class="mt-4 text-lg leading-relaxed text-blueGray-500">
            Поддържайте проектите си организирани, по план и на едно място с нашата лесна за използване платформа.
        </p>
        <div class="mt-12">
          <a on:click={redirectToLogin}
            class="clickable get-started text-white font-bold px-6 py-4 rounded outline-none focus:outline-none mr-1 mb-1 bg-red-400 active:bg-red-500 uppercase text-sm shadow hover:shadow-lg ease-linear transition-all duration-150"
          >
            Try today
          </a>
        </div>
      </div>
    </div>
  </div>

  <img
    class="absolute top-0 b-auto right-0 pt-16 sm:w-6/12 -mt-48 sm:mt-0 w-10/12 max-h-860-px"
    src="{patternVue}"
    alt="..."
  />
</section>

<style>
    .clickable {
        cursor: pointer;
    }
</style>