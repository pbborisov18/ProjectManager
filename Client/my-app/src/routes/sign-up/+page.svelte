<script>
    import {goto} from "$app/navigation";
    import Header from "$lib/components/Header.svelte";
    import {userEmail, loggedIn} from "$lib/stores.js";
    import { passwordMatch, containNumbers } from "./customValidators.js";
    import {resetCaptcha, handleCaptchaError} from "$lib/RecaptchaMethods.js";
    import toast from "svelte-french-toast";
    import {Button, Helper, Input} from "flowbite-svelte"
    import logo from "$lib/images/AlignLogo.png";
    import {PUBLIC_BACKEND_URL, PUBLIC_RECAPTCHA_KEY} from "$lib/Env.js";
    import {onMount} from "svelte";

    let registerButtonDisable = false;

    //Yes I reset recaptcha everywhere an error is thrown. Why?
    //Cuz the google script doesn't want to send any more requests in some cases when an error is thrown
    //So I'll just reset it everytime an error is thrown

    export const register = async(token) => {
        firstTry = false;

        if(handleInputPass() || handleInputPassConfirm()){
            registerButtonDisable = true;
            return;
        }

        const email = fields.email;
        const password = fields.pass;
        const confirmPassword = fields.confirmPass;

        fetch(PUBLIC_BACKEND_URL + '/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
                // 'X-XSRF-TOKEN': ffs
            },
            body: JSON.stringify({
                email: email,
                password: password,
                confirmPassword: confirmPassword
            }),
            credentials: "include"
        }).then(response => {
            if (response.status === 200) {
                //Enable the button inside there
                sendLoginRequestAfterRegister(email, password, token);
            } else if (PUBLIC_RECAPTCHA_KEY){
                resetCaptcha();
            }

            if(response.status === 400){
                registerButtonDisable = true;
                response.text().then(data => {
                    toast.error(data);
                });
            } else if(response.status === 500){
                registerButtonDisable = true;
                response.text().then(data => {
                    toast.error("Something went wrong!");
                });
            }
        }).catch(error => {
            registerButtonDisable = true;
            toast.error("Server is offline!");

            if(PUBLIC_RECAPTCHA_KEY){
                resetCaptcha();
            }
        });
    }

    function sendLoginRequestAfterRegister(email, password, token){
        const formData = new URLSearchParams();

        formData.append('email', email);
        formData.append('password', password);
        //We'll assume that the user wants to have a remember me token
        formData.append('rememberme', true);

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
                    toast.error(data.message);
                });
            }
            registerButtonDisable = true;
        }).catch(error => {
            registerButtonDisable = true;
            toast.error("Server is offline!");
            if (PUBLIC_RECAPTCHA_KEY){
                resetCaptcha();
            }
        });
    }

    let fields = {email: "", pass: "", confirmPass: ""};
    let errors = {email: "", pass: "", confirmPass: ""};
    let valid = false;
    let firstTry = true;

    function handleInputPass() {
        valid = false;

        if(fields.pass.length < 5){
            errors.pass = "Must be at least 5 characters long";
        } else if(!containNumbers(fields.pass, 2)){
            errors.pass = "Must contain at least 2 numbers";
        } else {
            errors.pass = "";
        }
    }

    function handleInputPassConfirm() {
        valid = false;

        if(!passwordMatch(fields.pass, fields.confirmPass) && fields.pass !== "" && fields.confirmPass !== ""){
            errors.confirmPass = "Not matching";
        } else {
            errors.confirmPass = "";
        }
    }

    onMount(() => {
        if (PUBLIC_RECAPTCHA_KEY){
            const script = document.createElement('script');
            script.src = 'https://www.google.com/recaptcha/api.js';
            script.async = true;
            script.defer = true;
            document.head.appendChild(script);

            window.handleCaptchaCallback = register;
            window.handleCaptchaError = handleCaptchaError;
            window.resetCaptcha = resetCaptcha;
        }
    });

    async function captcha(e) {
        e.preventDefault();
        firstTry = false;
        registerButtonDisable = true;
        if (!PUBLIC_RECAPTCHA_KEY) {
            register("");
        } else {
            grecaptcha.execute();
        }
    }

    function goToLoginPage() {
        goto("/login");
    }

</script>

<Header />

<main>
    <div class="registerPanel">
        <div class="logo">
            <img src="{logo}" alt="gfdgdf" draggable="false" >
        </div>
        <form on:submit={e => captcha(e)}>
            <Input class="mb-3" type="email" name="email" placeholder="Email" required bind:value={fields.email}/>

            {#if !valid && !firstTry}
                <Helper >{errors.pass}</Helper>
            {/if}
            <Input class="mb-3" type="password" name="password" placeholder="Password" bind:value={fields.pass} on:input={handleInputPass} required/>

            {#if !valid && !firstTry}
                <Helper>{errors.confirmPass}</Helper>
            {/if}
            <Input class="mb-8" type="password" name="confirmPassword" placeholder="Confirm password" bind:value={fields.confirmPass} on:input={handleInputPassConfirm} required />

            <Button type="submit" color="blue" disabled="{registerButtonDisable}">Register</Button>
        </form>
        <h3 class="mt-10 not-selectable clickable" on:click={goToLoginPage}>Already have an account?</h3>
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

    .registerPanel {
        margin: 0;
        border-radius: 5px;
        background-color: #F8F8F8;
        width: 330px;
        border: 0 solid #5A4A42;
        display: flex;
        flex-direction: column;
        align-items: center;
        font-family: sans-serif;
        font-weight: lighter;
        box-shadow: 0 0 1px 1px #BBBBBB;
        height: 450px;

        h3{
            text-decoration-line: underline;
        }

    }

    form {
        display: flex;
        width: 100%;
        flex-direction: column;
        align-items: center;
        padding: 30px 50px;
        font-weight: lighter;
    }

</style>