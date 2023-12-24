//First time this renders on the browser so it has access to the sessionStorage
//But after a refresh it renders on the server cuz idk. that's what it wants to do
//so sessionStorage doesn't exist in the server cuz you know that's how it works.

//Update: Everything renders on the server and gets sent to the browser the first
//you access the website. Refreshing counts as "the first time you access the website".
//This doesn't get triggered unless you aren't on this page when you refresh.
//if you are this gets executed on the server. that's why a refresh
//executes on the server. no fucking idea on how to circumvent that.
export async function load({fetch}) {
    let company = JSON.parse(sessionStorage.getItem("company")).businessUnit;

    const projectsResult = await fetch('http://localhost:8080/company/projects', {
        method: 'POST',
        headers: {
            'Content-Type': "application/json",
        },
        body: JSON.stringify(company),
        credentials: "include"
    }).catch(error => {
        console.log(error);
        return {
            error: 500
        };
    });

    if(projectsResult.status === 200){
        return {
            userBURoles: projectsResult.json()
        };
    }

    if(projectsResult.status !== 200){
        return {
            error: projectsResult.status
        };
    }
}