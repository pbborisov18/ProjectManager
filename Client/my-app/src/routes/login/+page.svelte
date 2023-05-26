<script>
    import {goto} from "$app/navigation";
    import Cookies from 'js-cookie';
    import Header from "$lib/components/Header.svelte";
    import {userEmail, loggedIn} from "$lib/stores.js";

    let errorText;

    import { useForm, Hint, HintGroup, validators, required, email } from "svelte-use-form";

    const form = useForm();

    const requiredMessage = "This field is required";

    Cookies.defaults = {
        sameSite: 'strict',
    };

    function login(event){
        event.preventDefault();

        const email = $form.email.value;
        const password = $form.password.value;

        const formData = new URLSearchParams();
        formData.append('email', email);
        formData.append('password', password);

        fetch('http://localhost:8080/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData,
            credentials: "include"
        })
            .then(response => {
                if (response.ok) {
                    userEmail.set(email);
                    loggedIn.set("true");
                    goto("/companies")
                } else if(response.redirected){
                    userEmail.set(email);
                    goto("/companies")
                } else if(response.status === 400){
                    response.text().then(text => {
                        errorText = text;
                        console.log(errorText);
                        throw new Error('Login failed');
                    });
                }
            }).catch(error => {
            console.error(error);
        });
    }

    function goToRegisterPage(){
        goto("/sign-up");
    }

</script>

<Header />
<main>
    <div class="loginPanel">
        {#if (errorText) }
            <p class="error">{errorText}</p>
        {/if}
        <form use:form>
            <HintGroup for="email">
                <Hint on="required">{requiredMessage}</Hint>
                    <Hint on="email" hideWhenRequired>Has to be a valid email</Hint>
            </HintGroup>
            <label>
                <input type="text" name="email" placeholder="Email" use:validators={[required, email]}/>
            </label>
            <label>
                <input type="password" name="password" placeholder="Login" />
            </label>
            <button disabled={!$form.valid} on:click|preventDefault={login}>Login</button>
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
    height: 100vh;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

  }

  .clickable{
      cursor: pointer;
  }

  .logo {
    display: flex;
    align-items: center;
    color: #38302b;

    img {
      width: 100px;
    }
  }

  .not-selectable {
    -webkit-user-select: none; /* Chrome, Safari, Opera */
    -moz-user-select: none; /* Firefox */
    -ms-user-select: none; /* IE 10+ */
    user-select: none; /* Standard syntax */
  }

  .loginPanel {
    border-radius: 5px;
    background-color: #F8F8F8;
    width: 300px;
    border: 0 solid #5A4A42;
    display: flex;
    flex-direction: column;
    align-items: center;
    font-family: Bahnschrift, monospace;
    font-weight: lighter;
    box-shadow: 0px 0px 1px 1px #BBBBBB;
    height: 270px;
    transform: translateY(-50px);

      h3{
          margin-bottom: 1vh;
          text-decoration-line: underline;
      }

  }

  h2 {
    padding-top: 20px;
    font-weight: lighter;
    font-family: Bahnschrift, monospace;
  }

  form {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 40px 20px 20px;
    font-weight: lighter;

    input {
      border-radius: 0.3em;
      border: solid 1px #5A4A42;
      padding: 5px;
    }

    label {
      margin-bottom: 20px;
    }

    button {
      border: solid 0 #5A4A42;
      background-color: #0037B6;
      color: #FFFFFF;
      font-family: Bahnschrift, monospace;
      padding: 7px 15px;
      border-radius: 0.5em;
      font-weight: normal;
      cursor: pointer;
    }
  }
</style>

