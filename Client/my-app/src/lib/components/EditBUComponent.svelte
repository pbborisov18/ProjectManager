<script>
    import {Button, Input, Label} from "flowbite-svelte";
    import {goto} from "$app/navigation";

    export let BURole;
    export let onChangeName;
    let BUEditName = BURole.businessUnit.name;

    function editBU(){
        if(!BUEditName){
            alert("The field can't be empty!");
        }else {
            let updatedBURole = {
                ...BURole.businessUnit,
                name: BUEditName
            };
            fetch('http://localhost:8080/updateCompany', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedBURole),
                credentials: "include"
            }).then(response=>{
                if(response.ok){
                    BURole.name = BUEditName;
                    onChangeName(BUEditName);
                } else if(response.status === 400){
                    response.text().then(text => {
                        throw new Error(text);
                    })
                } else if(response.status === 401){
                    response.text().then(text => {
                        throw new Error(text);
                    });
                    goto("/login");
                } else if(response.status === 403){
                    response.text().then(text => {
                        throw new Error(text);
                    });
                } else if(response.status === 500){
                    response.text().then(text => {
                        throw new Error(text);
                    });
                }
            }).catch(error => {
                alert(error);
            });
        }
    }

</script>

        <div class="">
            <div class=" mt-5">
                <Label for="buName" class="text-black ml-10">Name of business unit</Label>
                <Input class="text-black ml-10 max-w-[250px]" type="text" id="buName" bind:value={BUEditName} required/>
            </div>
        </div>
        <div class="">
            <Button class="ml-10 mt-5" color="blue" type="submit" on:click={editBU}>Edit</Button>
        </div>

<style lang="scss">

</style>