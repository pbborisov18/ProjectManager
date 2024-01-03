<script>

    import {onMount} from "svelte";
    import {loggedIn, userEmail} from "$lib/stores.js";
    import {goto} from "$app/navigation";
    import {
        Button,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        TableHead,
        TableHeadCell,
        TableSearch
    } from "flowbite-svelte";
    import { slide } from 'svelte/transition';
    import CustomBadge from "$lib/components/CustomBadge.svelte";

    export let BURole;
    let youSure = false;
    let searchTerm = '';
    let usersList = [];
    let filteredUsersList = [];

    $: {
        filteredUsersList = usersList.filter((item) => item.email.toLowerCase().indexOf(searchTerm.toLowerCase()) !== -1);
    }

    $: searchTerm && test();

    function test(){
        openRow = null;
        lastOpenRow = null;
    }

    let selectedUser;

    function getLast30UsersWhoJoinedBU(){
        fetch('http://localhost:8080/bu/getLast30Users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(BURole.businessUnit),
            credentials: "include"
        }).then(response=>{
            if (response.status === 200) {
                response.json().then(data => {
                    usersList = data;
                });
            } else if(response.status === 400){
                // notification
            } else if(response.status === 401){
                // notification
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 403){
                alert("No permission");
            } else if(response.status === 500){
                // notification
            }
        }).catch(error => {
            // server died or something
        });
    }

    function getRoles(user){
        return new Promise((resolve, reject) => {
            fetch('http://localhost:8080/bu/getUserRoles', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    businessUnit: BURole.businessUnit,
                    user: user
                }),
                credentials: "include"
            }).then(response => {
                if (response.status === 200) {
                    response.json().then(data => {
                        resolve(data);
                    });
                } else if (response.status === 400) {
                    // notification
                } else if (response.status === 401) {
                    // notification
                    userEmail.set("");
                    loggedIn.set("");
                    goto("/login");
                } else if (response.status === 403) {
                    alert("No permission");
                } else if (response.status === 500) {
                    // notification
                }
            }).catch(error => {
                reject();
                // server died or something
            });
        });
    }

    //Practically copypaste to the above function. Just easier to do this instead of figuring out how to use the above
    function searchForUserInBU(user){
        fetch('http://localhost:8080/bu/getUserRoles', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                businessUnit: BURole.businessUnit,
                user: user
            }),
            credentials: "include"
        }).then(response => {
            if (response.status === 200) {
                response.json().then(data => {
                    searchTerm = "";

                    usersList = [data.userBusinessUnit.user, ...usersList];
                    filteredUsersList = usersList;

                    rolesOnSelectedUser = data.userBusinessUnit.roles;
                    allRoles = data.roles;
                    allRoles = allRoles.filter(r1 => !rolesOnSelectedUser.some(r2 => r1.name === r2.name));

                    openRow = 0;
                    lastOpenRow = 0;
                });
            } else if (response.status === 400) {
                // notification
                alert("User not found");
            } else if (response.status === 401) {
                // notification
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if (response.status === 403) {
                alert("No permission");
            } else if (response.status === 500) {
                // notification
            }
        }).catch(error => {
            // server died or something
        });
    }

    function saveRoles() {
        selectedUser = {...selectedUser, userBusinessUnit: {...selectedUser.userBusinessUnit, roles: rolesOnSelectedUser}};

        fetch('http://localhost:8080/bu/changeUserRoles', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(selectedUser.userBusinessUnit),
            credentials: "include"
        }).then(response => {
            if (response.status === 200) {
                originalRolesOnSelectedUser = rolesOnSelectedUser;
                // notification
            } else if (response.status === 400) {
                // notification
                alert("User not found");
            } else if (response.status === 401) {
                // notification
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if (response.status === 403) {
                alert("No permission");
            } else if (response.status === 500) {
                // notification
            }
        }).catch(error => {
            // server died or something
        });
    }

    function kickUser(user) {
        fetch('http://localhost:8080/bu/kickUser', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                businessUnit: BURole.businessUnit,
                user: user
            }),
            credentials: "include"
        }).then(response => {
            if (response.status === 200) {
                searchTerm = "";

                usersList = usersList.filter(u => u.email !== user.email);
                filteredUsersList = usersList;

                rolesOnSelectedUser = null;
                allRoles = null;
                openRow = null;
                lastOpenRow = null;
            } else if (response.status === 400) {
                // notification
                alert("User not found");
            } else if (response.status === 401) {
                // notification
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if (response.status === 403) {
                alert("No permission");
            } else if (response.status === 500) {
                // notification
            }
        }).catch(error => {
            // server died or something
        });
    }

    onMount(() => {
        getLast30UsersWhoJoinedBU();
    });

    let openRow = null;
    let lastOpenRow = null;
    let allRoles = [];
    let rolesOnSelectedUser = [];
    let originalRolesOnSelectedUser = [];
    //This opens to a huge ass bug if the user decides to fuck around with the search
    //"Fixed". I clear them every time something changes in the searchbar
    async function toggleRow (i, item) {

        let toOpenRow = openRow === i ? null : i;

        //How does null >= 0 is true...
        if((toOpenRow !== null && toOpenRow >= 0) && lastOpenRow !== toOpenRow) {
            selectedUser = await getRoles(item);

            if (selectedUser) {
                rolesOnSelectedUser = selectedUser.userBusinessUnit.roles;
                originalRolesOnSelectedUser = rolesOnSelectedUser;
                allRoles = selectedUser.roles;
                allRoles = allRoles.filter(r1 => !rolesOnSelectedUser.some(r2 => r1.name === r2.name));
            }
        }

        openRow = toOpenRow;

        if(toOpenRow !== null) {
            lastOpenRow = openRow;
        }

    }

    function moveToRolesOnSelectedUser(role){
        allRoles = allRoles.filter(r => r.name !== role.name);
        rolesOnSelectedUser = [...rolesOnSelectedUser, role];
    }

    function moveToAllRoles(role){
        rolesOnSelectedUser = rolesOnSelectedUser.filter(r => r.name !== role.name);
        allRoles = [role, ...allRoles];
    }

    function arrayEqualUnordered(arr1, arr2) {
        const sortedArr1 = JSON.stringify(arr1.slice().sort());
        const sortedArr2 = JSON.stringify(arr2.slice().sort());

        return sortedArr1 === sortedArr2;
    }

    function returnToOriginalRoles() {
        rolesOnSelectedUser = originalRolesOnSelectedUser;
        allRoles = selectedUser.roles;
        allRoles = allRoles.filter(r1 => !rolesOnSelectedUser.some(r2 => r1.name === r2.name));
    }

</script>

{#if usersList}
    <TableSearch shadow bind:inputValue={searchTerm}>

        {#if filteredUsersList.length === 0}
            <TableHead>
                <Button color="blue" on:click={() => searchForUserInBU({id: null, email: searchTerm})}>Search</Button>
            </TableHead>
        {:else}
            <TableHead>
                <TableHeadCell>Email (last 30 users joined)</TableHeadCell>
            </TableHead>
        {/if}
        <TableBody class="divide-y">
            {#each filteredUsersList as item, i}

                <TableBodyRow class="cursor-pointer" on:click={() => {toggleRow(i, item)}}>
                    <TableBodyCell class="select-none">{item.email}</TableBodyCell>
                </TableBodyRow>

                {#if openRow === i}
                    <TableBodyRow>
                        <TableBodyCell colspan="4" class="p-0">
                            <div class="px-2 py-3" transition:slide={{ duration: 300, axis: 'y' }}>
                                <p>Current roles</p>
                                <div class="flex-wrap flex gap-1 mb-5" >
                                    {#if BURole.authorityDTOList.some(a => a.name === "ChangePermissions")}
                                        {#each rolesOnSelectedUser as role}
                                            <CustomBadge onClick={() => moveToAllRoles(role)} deleteButton="{true}" toDisplay="{role.name}"/>
                                        {/each}
                                    {:else}
                                        {#each rolesOnSelectedUser as role}
                                            <CustomBadge deleteButton="{true}" toDisplay="{role.name}"/>
                                        {/each}
                                    {/if}
                                </div>
                                <p>All other roles</p>
                                <div class="flex-wrap flex gap-1">
                                    {#if BURole.authorityDTOList.some(a => a.name === "ChangePermissions")}
                                        {#each allRoles as role}
                                            <CustomBadge onClick={() => moveToRolesOnSelectedUser(role)} deleteButton="{true}" toDisplay="{role.name}"/>
                                        {/each}
                                    {:else}
                                        {#each allRoles as role}
                                            <CustomBadge deleteButton="{true}" toDisplay="{role.name}"/>
                                        {/each}
                                    {/if}
                                </div>
                                {#if BURole.authorityDTOList.some(a => a.name === "ChangePermissions")}
                                    <Button color="blue" class="mt-5" on:click={saveRoles} disabled="{arrayEqualUnordered(originalRolesOnSelectedUser, rolesOnSelectedUser)}">Save</Button>
                                    <Button color="blue" class="mt-5" on:click={returnToOriginalRoles} disabled="{arrayEqualUnordered(originalRolesOnSelectedUser, rolesOnSelectedUser)}">Cancel</Button>
                                    {#if youSure}
                                        <Button color="red" class="mt-5" on:click={kickUser(item)}>You sure?</Button>
                                        <Button color="red" class="mt-5" on:click={() => youSure = false}>No</Button>
                                    {:else}
                                        <Button color="red" class="mt-5" on:click={() => youSure = true}>Kick</Button>
                                    {/if}
                                {/if}

                            </div>
                        </TableBodyCell>
                    </TableBodyRow>
                {/if}

            {/each}
        </TableBody>
    </TableSearch>
{/if}

<style lang="scss">
    .not-selectable {
        -webkit-user-select: none; /* Chrome, Safari, Opera */
        -moz-user-select: none; /* Firefox */
        -ms-user-select: none; /* IE 10+ */
        user-select: none; /* Standard syntax */
    }
</style>