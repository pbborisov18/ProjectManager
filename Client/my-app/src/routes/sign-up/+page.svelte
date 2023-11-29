<script>
    import {goto} from "$app/navigation";
    import Header from "$lib/components/Header.svelte";
    import {userEmail, loggedIn} from "$lib/stores.js";


    import { useForm, Hint, HintGroup, validators, required, minLength, email } from "svelte-use-form";
    import { passwordMatch, containNumbers } from "./customValidators.js";

    const form = useForm();

    const requiredMessage = "This field is required";

    function register(event) {
        event.preventDefault();

        const email = $form.email.value;
        const password = $form.password.value;
        const confirmPassword = $form.confirmPassword.value;

        fetch('http://localhost:8080/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                // 'X-XSRF-TOKEN': ffs
            },
            body: JSON.stringify({ email, password, confirmPassword }),
            credentials: "include"
        }).then(response => {
            if (!response.ok) {
                throw new Error('Registration failed');
            }
        }).then(() => {
            sendLoginRequestAfterRegister(email, password);
        }).catch(error => {
            console.error(error);
            alert('Failed to register! ' + error);
        });
    }

    function sendLoginRequestAfterRegister(email, password){
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
                    goto("/companies")
                } else if(response.status === 400){
                    throw new Error('Login failed');
                }
            }).catch(error => {
            console.error(error);
            alert('Login failed' + error);
        });
    }

    function goToLoginPage(){
        goto("/login");
    }

</script>

<Header />

<main>
    <div class="registerPanel">
        <form use:form>
            <HintGroup for="email">
                <Hint on="required">{requiredMessage}</Hint>
                <Hint on="email" hideWhenRequired>Has to be a valid email!</Hint>
            </HintGroup>
            <label>
                <input type="text" name="email" placeholder="Email" use:validators={[required, email]}/>
            </label>

            <HintGroup for="password">
                <Hint on="required">{requiredMessage}</Hint>
                <Hint on="minLength" hideWhenRequired let:value>Password too short (minimum{value}).</Hint>
                <Hint on="containNumbers" hideWhen="minLength" let:value>
                    Has to have at least {value} numbers.
                </Hint>
            </HintGroup>
            <label>
                <input type="password" name="password" placeholder="Password" use:validators={[required, minLength(5), containNumbers(2)]}/>
            </label>

            <HintGroup for="confirmPassword">
                <Hint on="required">{requiredMessage}</Hint>
                <Hint on="passwordMatch" hideWhenRequired>Passwords aren't the same!</Hint>
            </HintGroup><br />
            <label>
                <input type="password" name="confirmPassword" placeholder="Confirm password" use:validators={[required, passwordMatch]} />
            </label>


            <button disabled={!$form.valid} on:click|preventDefault={register}>Register</button>
        </form>
        <h3 class="not-selectable clickable" on:click={goToLoginPage}>Already have an account?</h3>
    </div>
</main>

<style lang="scss">
  :root{
    background-color: #F8F8F8;
      overflow: hidden;
  }

  .clickable{
      cursor: pointer;
  }

  main {
    height: 100vh;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

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

  .registerPanel {
    border-radius: 5px;
    background-color: #F8F8F8;
    width: 300px;
    border: 0 solid #5A4A42;
    display: flex;
    flex-direction: column;
    align-items: center;
    font-family: sans-serif;
    font-weight: lighter;
    box-shadow: 0px 0px 1px 1px #BBBBBB;
      height: 400px;
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
    padding: 50px 20px 20px;
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