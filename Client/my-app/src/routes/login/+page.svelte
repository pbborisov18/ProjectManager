<script>
    import {goto} from "$app/navigation";
    import Header from "$lib/components/Header.svelte";
    import {userEmail, loggedIn} from "$lib/stores.js";
    import toast from "svelte-french-toast";
    import {Button, Checkbox, Input} from "flowbite-svelte";
    import logo from "$lib/images/AlignLogo.png";

    let checkBox = false;

    function login(event){
        event.preventDefault();

        const email = event.target.email.value;
        const password = event.target.password.value;

        const formData = new URLSearchParams();
        formData.append('email', email);
        formData.append('password', password);
        formData.append('rememberme', checkBox);

        fetch('http://localhost:8080/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData,
            credentials: "include"
        }).then(response => {
            if (response.status === 200) {
                userEmail.set(email);
                loggedIn.set("true");
                goto("/companies")
            } else if(response.status === 400){
                response.text().then(data => {
                    toast.error(data);
                });
            } else if(response.status === 500){
                response.json().then(data => {
                    toast.error("Server failed!");
                });
            }
        }).catch(error => {
            toast.error("Something went wrong!");
        });
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

        <form on:submit={e => login(e)}>
            <Input class="mb-3" type="email" name="email" placeholder="Email" required/>
            <Input class="mb-3" type="password" name="password" placeholder="Password" required/>
            <div class="w-[100%] mb-3">
                <Checkbox bind:checked={checkBox}>Remember me</Checkbox>
            </div>
            <Button type="submit" color="blue">Login</Button>

        </form>
        <h3 class="not-selectable clickable" on:click={goToRegisterPage}>No account?</h3>
    </div>
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

