<script>
    import {Button, Input, Label} from "flowbite-svelte";
    import {goto} from "$app/navigation";
    import {onMount} from "svelte";
    import plusIcon from "$lib/images/plus.png";
    import CustomBadge from "$lib/components/CustomBadge.svelte";
    import CustomAuthoritySettingsLabel from "$lib/components/CustomAuthoritySettingsLabel.svelte";
    import {userEmail, loggedIn} from "$lib/stores";
    import toast from "svelte-french-toast";
    import {PUBLIC_BACKEND_URL} from "$lib/Env.js";

    export let BURole;

    let roles = [];
    //TODO: Update to keeping the original role and checking against it if it's edited
    //let edited = false;

    let authoritiesDisplay = [];

    let selectedRole;
    let newRoleToggle = false;

    function addOrRemoveAuthorities(event, authority) {
        // edited = true;
        const checked = event.detail;
        const authorityToCheck = {id: authority.id, name: authority.name};

        if (checked) {
            if (!selectedRole.authorities.some(a => a.id === authorityToCheck.id)) {
                selectedRole.authorities= [...selectedRole.authorities, authorityToCheck];
            }
        } else {
            selectedRole.authorities = selectedRole.authorities.filter(a => a.id !== authorityToCheck.id);
        }
    }

    //TODO: Do a fetch to the backend (Make an endpoint for this)
    function setupAllAuthoritiesList(){
        authoritiesDisplay = [
            {
                "id": 2,
                "name": "UpdateBU",
                "title": "Update business unit",
                "body": "Ability to edit stuff from General Settings tab.",
                //this is inefficient but ehh... (can make an array with booleans or something)
                "checked": selectedRole.authorities.some(a => a.name === "UpdateBU")
            },
            {
                "id": 3,
                "name": "DeleteBU",
                "title": "Delete business unit",
                "body": "Ability to delete business unit.",
                "checked": selectedRole.authorities.some(a => a.name === "DeleteBU")
            },
            {
                "id": 1,
                "name": "CreateChildren",
                "title": "Create children business units",
                "body": "Ability to create children business units. Projects for companies and teams for projects.",
                "checked": selectedRole.authorities.some(authority => authority.name === "CreateChildren")
            },
            {
                "id": 8,
                "name": "CreateWhiteboard",
                "title": "Create whiteboard",
                "body": "Ability to create a whiteboard.",
                "checked": selectedRole.authorities.some(authority => authority.name === "CreateWhiteboard")
            },
            {
                "id":9,
                "name": "ManageWhiteboard",
                "title": "Manage whiteboard",
                "body": "Ability to edit and delete whiteboard and its columns.",
                "checked": selectedRole.authorities.some(authority => authority.name === "ManageWhiteboard")
            },
            {
                "id": 10,
                "name": "InteractWithWhiteboard",
                "title": "Interact with whiteboard",
                "body": "Ability to interact with the notes on the whiteboard. Allows you to create, edit and delete notes.",
                "checked": selectedRole.authorities.some(authority => authority.name === "InteractWithWhiteboard")
            },
            {
                "id": 4,
                "name": "ChangePermissions",
                "title": "Change roles and users",
                "body": "Ability to edit the roles and users of a business unit.",
                "checked": selectedRole.authorities.some(authority => authority.name === "ChangePermissions")

            },
            {
                "id": 5,
                "name": "SeePermissions",
                "title": "See roles and users",
                "body": "Ability to read the roles and users of a business unit.",
                "checked": selectedRole.authorities.some(authority => authority.name === "SeePermissions")
            },
            {   "id": 7,
                "name": "ManageSentInvites",
                "title": "Manage business unit invites",
                "body": "Ability to send, see and delete invites to the business unit.",
                "checked": selectedRole.authorities.some(authority => authority.name === "ManageSentInvites")
            }
        ];
    }

    function selectRole(role){
        // edited = false;
        newRoleToggle = false;
        selectedRole = {id: role.id, name: role.name, authorities: role.authorities, businessUnit: BURole.businessUnit};
        setupAllAuthoritiesList();
    }

    function findAllRoles(){

        fetch(PUBLIC_BACKEND_URL + '/roles', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(BURole.businessUnit),
            credentials: "include"
        }).then(response=>{
            if (response.status === 200) {
                response.json().then(data => {
                    roles = data;
                });
            } else if(response.status === 400){
                response.text().then(data => {
                    toast.error("Bad request!");
                });
            } else if(response.status === 401){
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 403){
                toast.error("No permission!");
            } else if(response.status === 500){
                response.text().then(data => {
                    toast.error("Something went wrong");
                });
            }
        }).catch(error => {
            toast.error("Server is offline!");
        });
    }

    function deleteRole(role){
        fetch(PUBLIC_BACKEND_URL + '/deleteRole', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(role),
            credentials: "include"
        }).then(response=>{
            if (response.status === 200) {
                roles = roles.filter(r => r.id !== role.id);
                selectedRole = null;
                toast.success("Role deleted!");
            } else if(response.status === 400){
                toast.error("Bad request!");
            } else if(response.status === 401){
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 403){
                toast.error("No permission!");
            } else if(response.status === 500){
                response.text().then(data => {
                    toast.error("Something went wrong");
                });
            }
        }).catch(error => {
            toast.error("Server is offline!");
        });
    }

    function editRole(role){
        fetch(PUBLIC_BACKEND_URL + '/updateRole', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(role),
            credentials: "include"
        }).then(response=>{
            if (response.status === 200) {
                response.json().then(data => {
                    const roleIndex = roles.findIndex(r => r.id === role.id);
                    roles[roleIndex] = data;
                    selectRole(data);
                    toast.success("Role edited!");
                });
            } else if(response.status === 400){
                response.text().then(data => {
                    toast.error("Bad request!");
                });
            } else if(response.status === 401){
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 403){
                toast.error("No permission!");
            } else if(response.status === 500){
                response.text().then(data => {
                    toast.error("Something went wrong");
                });
            }
        }).catch(error => {
            toast.error("Server is offline!");
        });
    }

    function createRole(role){
        fetch(PUBLIC_BACKEND_URL + '/createRole', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(role),
            credentials: "include"
        }).then(response=>{
            if (response.status === 200) {
                response.json().then(data => {
                    roles = [...roles, data];
                    selectedRole = data;
                    newRoleToggle = false;
                    toast.success("Role created!");
                });
            } else if(response.status === 400){
                response.text().then(data => {
                    toast.error("Bad request!");
                });
            } else if(response.status === 401){
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 403){
                toast.error("No permission!");
            } else if(response.status === 500){
                response.text().then(data => {
                    toast.error("Something went wrong");
                });
            }
        }).catch(error => {
            toast.error("Server is offline!");
        });
    }

    // Awful idea as the component gets re-rendered pretty much every time the user clicks the role "tab"
    onMount(()=>{
        findAllRoles();
    });

</script>

{#if BURole.authorityDTOList.some(a => a.name === "ChangePermissions" || a.name === "SeePermissions")}
    <div class="mainRoleDiv flex-wrap flex gap-1">
        {#if BURole.authorityDTOList.some(a => a.name === "ChangePermissions")}
            <div class="plusDiv clickable not-selectable"
                 on:click="{() => {
                        newRoleToggle = true;
                        selectedRole = {
                                'id': null,
                                'businessUnit': BURole.businessUnit,
                                'name': '',
                                'authorities': []
                            };
                        setupAllAuthoritiesList();
                 }}">
                <img src="{plusIcon}"/>
            </div>
            {#each roles as role}
                <CustomBadge toDisplay="{role.name}"
                             onClick="{() => selectRole(role)}"
                             onDestroy="{() => deleteRole(role)}"
                             deleteButton="{role.name === 'Default' || role.name === 'Admin'}"/>
            {/each}
        {:else}
            {#each roles as role}
                <CustomBadge toDisplay="{role.name}"
                             onClick="{() => selectRole(role)}"
                             onDestroy="{() => deleteRole(role)}"
                             deleteButton="{true}"/> <!-- this is backwards for some reason -->
            {/each}
        {/if}
    </div>

    <form>
        <div>
            <hr>
            {#if selectedRole || newRoleToggle}
                <div class="mt-5 mb-5">
                    <Label for="roleName" class="text-black ml-10">Role name</Label>
                    {#if selectedRole.name === "Default" || selectedRole.name === "Admin" || !BURole.authorityDTOList.some(a => a.name === "ChangePermissions")}
                        <Input class="text-black ml-10 max-w-[250px]" type="text" id="roleName" required disabled bind:value={selectedRole.name}/>
                    {:else}
                        <Input class="text-black ml-10 max-w-[250px]" type="text" id="roleName" required bind:value={selectedRole.name}/>
                    {/if}
                </div>
                {#if BURole.authorityDTOList.some(a => a.name === "ChangePermissions")}
                    {#each authoritiesDisplay as authority}
                        <CustomAuthoritySettingsLabel onToggle={(event) => addOrRemoveAuthorities(event, authority)} toggled={authority.checked} title={authority.title} body={authority.body}/>
                        <hr/>
                    {/each}
                {:else}
                    {#each authoritiesDisplay as authority}
                        <CustomAuthoritySettingsLabel disabled="{true}" onToggle={(event) => addOrRemoveAuthorities(event, authority)} toggled={authority.checked} title={authority.title} body={authority.body}/>
                        <hr/>
                    {/each}
                {/if}

                <div>
                    {#if !newRoleToggle && selectedRole.name !== "Default" && selectedRole.name !== "Admin" && BURole.authorityDTOList.some(a => a.name === "ChangePermissions")}
                        <Button class="mt-5 ml-10" color="red" on:click={() => deleteRole(selectedRole)}>Delete</Button>
                    {/if}
                    {#if !newRoleToggle && BURole.authorityDTOList.some(a => a.name === "ChangePermissions")}
                        <Button disabled="{selectedRole.name === '' || selectedRole.authorities.length < 1}" class="mt-5 ml-10" color="green" on:click={() => {
                            selectedRole.name = document.getElementById("roleName").value;
                            editRole(selectedRole);
                        }}>Save</Button>
                    {:else if newRoleToggle}
                        <Button disabled="{selectedRole.name === '' || selectedRole.authorities.length < 1}" class="mt-5 ml-10" color="green" on:click={() => {
                        selectedRole.name = document.getElementById("roleName").value;
                        createRole(selectedRole);
                    }}>Create</Button>
                        {:else}
                        <Button  class="mt-5 ml-10" color="green" disabled="{true}">Save</Button>
                    {/if}

                <!--{#if edited === true}-->
                <!--    <p>Not saved</p>-->
                <!--{/if}-->
            </div>

            {/if}
        </div>
    </form>
{/if}



<style lang="scss">

        .mainRoleDiv{
            width: 100%;
            max-height: 20vh;
            overflow-y: auto;
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

        img{
            max-width: 30px;
            padding: 5px;
        }

        .plusDiv{
            display: flex;
            border-radius: 50%;
            border: 2px solid black;
            align-items: center;
            justify-content: center;
            padding: 1vh;
        }

        hr {
            display: block;
            height: 1px;
            border: 0;
            border-top: 1px solid #ccc;
            margin: 1em 0;
            padding: 0;
        }

</style>