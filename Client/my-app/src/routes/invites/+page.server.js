export async function load({ fetch }) {
    const invitesResult = await fetch(
        'http://localhost:8080/invites?' + new URLSearchParams({inviteState:'PENDING'}), {
            method: 'GET',
            headers: {
                'Content-Type': "application/json",

            },
            credentials: "include"
        })
        .catch(() =>{
            return{
                error : 500
            };
        });

    if(invitesResult.status === 200){
        return{
            invites: invitesResult.json()
        };
    }

    if(!invitesResult.status.ok){
        return {
          error: invitesResult.status
        };
    }
}