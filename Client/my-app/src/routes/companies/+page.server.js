export async function load({ fetch, cookies }) {
    const companiesResult = await fetch('http://localhost:8080/companies', {
        method: 'GET',
        headers: {
            'Content-Type': "application/json",
            credentials: "include"
        }}).catch(error =>{
            return {
                error : 500
            };
        });

    // console.log(companiesResult.status);

    if (companiesResult.status === 200) {
        return {
            userBURoles: companiesResult.json()
        };
    }

    //204 = No content
    if(companiesResult.status === 204){

        const userResult = await fetch('http://localhost:8080/authenticatedUser', {
            method: 'GET',
            headers: {
                'Content-Type': "application/json",
                credentials: "include"
            }})

        if(userResult.status === 204){
            return {
                error: 401
            };
        }

        if(userResult.status === 500){
            return {
                error: 500
            };
        }

        return {
            user: userResult.json(),
            error: 204
        };
    }


    if(!companiesResult.status.ok){
        return {
            error: companiesResult.status
        };
    }


}