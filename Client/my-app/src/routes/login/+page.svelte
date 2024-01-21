<script>
    import {goto} from "$app/navigation";
    import Header from "$lib/components/Header.svelte";
    import {userEmail, loggedIn} from "$lib/stores.js";
    import toast from "svelte-french-toast";
    import {Button, Checkbox, Input} from "flowbite-svelte";
    import logo from "$lib/images/AlignLogo.png";
    import {PUBLIC_BACKEND_URL, PUBLIC_RECAPTCHA_KEY} from "$lib/Env.js";
    import {onMount} from "svelte";
    import {handleCaptchaError, resetCaptcha} from "$lib/RecaptchaMethods.js";

    let checkBox = false;
    let email = "";
    let password = "";
    let loginButtonDisabled = false;

    export const login = async(token) => {

        const formData = new URLSearchParams();
        formData.append('email', email);
        formData.append('password', password);
        //if I send rememberme: false, I still get a rememberme cookie just cuz ...
        if (checkBox) {
            formData.append('rememberme', checkBox);
        }

        let headersToSend;

        if(token){
            headersToSend = {
                'Content-Type': 'application/x-www-form-urlencoded',
                'Recaptcha-Token': token
            }
        } else {
            headersToSend = {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }

        fetch(PUBLIC_BACKEND_URL + '/login', {
            method: 'POST',
            headers: headersToSend,
            body: formData,
            credentials: "include"
        }).then(response => {
            if (response.status === 200) {
                userEmail.set(email);
                loggedIn.set("true");
                goto("/companies")
            } else if (PUBLIC_RECAPTCHA_KEY){
                resetCaptcha();
            }

            if(response.status === 400){
                response.text().then(data => {
                    toast.error(data);
                });
            } else if(response.status === 500){
                response.json().then(data => {
                    toast.error("Server failed!");
                });
            }
            loginButtonDisabled = false;
        }).catch(error => {
            loginButtonDisabled = false;
            toast.error("Something went wrong!");
            if (PUBLIC_RECAPTCHA_KEY){
                resetCaptcha();
            }
        });
    }

    onMount(() => {
        if (PUBLIC_RECAPTCHA_KEY) {
            const script = document.createElement('script');
            script.src = 'https://www.google.com/recaptcha/api.js';
            script.async = true;
            script.defer = true;
            document.head.appendChild(script);

            window.handleCaptchaCallback = login;
            window.handleCaptchaError = handleCaptchaError;
            window.resetCaptcha = resetCaptcha;
        }
    });

    async function captcha(e){
        e.preventDefault();
        loginButtonDisabled = true;
        if (!PUBLIC_RECAPTCHA_KEY) {
            login("");
        } else {
            grecaptcha.execute();
        }
    }

    function goToRegisterPage(){
        goto("/sign-up");
    }

</script>

<Header/>
<main>
    <div class="loginPanel">
        <div class="logo">
            <img src="{logo}" alt="gfdgdf" draggable="false" >
        </div>

        <form on:submit={e => captcha(e)}>
            <Input class="mb-3" type="email" name="email" placeholder="Email" bind:value={email} required/>
            <Input class="mb-3" type="password" name="password" placeholder="Password" bind:value={password} required/>
            <div class="w-[100%] mb-3">
                <Checkbox bind:checked={checkBox}>Remember me</Checkbox>
            </div>

            <Button type="submit" color="blue" disabled="{loginButtonDisabled}">Login</Button>
        </form>
        <h3 class="not-selectable clickable" on:click={goToRegisterPage}>No account?</h3>
    </div>
    {#if PUBLIC_RECAPTCHA_KEY}
        <div
            class="g-recaptcha"
            data-sitekey={PUBLIC_RECAPTCHA_KEY}
            data-callback="handleCaptchaCallback"
            data-size="invisible"
        />
    {/if}
</main>

<style lang="scss">
    :root{
        background-color: #F8F8F8;
        overflow: hidden;
    }

    main {
        height: 93vh; //(header is 7vh)
        display: flex;
        justify-content: center;
        align-items: center;
        background-color: #F8F8F8;
        margin: 0;
    }

    .clickable{
        cursor: pointer;
    }

    .logo {
        display: flex;
        align-items: center;
        color: #38302b;
        margin-top: 5px;
        opacity: 0.9;

        img {
            width: 70px;
        }
    }

    .not-selectable {
        -webkit-user-select: none; /* Chrome, Safari, Opera */
        -moz-user-select: none; /* Firefox */
        -ms-user-select: none; /* IE 10+ */
        user-select: none; /* Standard syntax */
    }

    .loginPanel {
        margin: 0;
        border-radius: 5px;
        background-color: #F8F8F8;
        width: 330px;
        border: 0 solid #5A4A42;
        display: flex;
        flex-direction: column;
        align-items: center;
        font-family: Bahnschrift, monospace;
        font-weight: lighter;
        box-shadow: 0 0 1px 1px #BBBBBB;
        height: 330px;

        h3{
            text-decoration-line: underline;
        }

    }

    form {
        display: flex;
        width: 100%;
        flex-direction: column;
        align-items: center;
        padding: 20px 50px;
        font-weight: lighter;
    }

</style>

