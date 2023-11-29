export async function load({ fetch }) {

    const companiesResult = await fetch('http://localhost:8080/companies', {
        method: 'GET',
        headers: {
            'Content-Type': "application/json",
            credentials: "include"
        }}).catch( () => {
            return {
                error : 500
            };
        });


    if (companiesResult.status === 200) {
        return {
            BURoles: companiesResult.json()
        };
    }

    if(!companiesResult.status.ok){
        return {
            error: companiesResult.status
        };
    }


}