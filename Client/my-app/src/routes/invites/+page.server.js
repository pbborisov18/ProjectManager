export async function load({ fetch, cookies }) {
    const invitesResult = await fetch('http://localhost:8080/invites', {
        method: 'GET',
        headers: {
            'Content-Type': "application/json",
            'JSESSION': cookies.get('token')
        }}).catch(error =>{
            return{
                error : 500
            };
        });

    if(invitesResult.status === 200){
        return{
            invites: invitesResult.json()
        };
    }

    if(invitesResult.status === 204){
        return{
            error: 204
        };
    }

    if(!invitesResult.status.ok){
        return {
            //Can be 400, 401, 403, 500
          error: invitesResult.status
        };
    }
}