export async function load({fetch}) {
    let project = JSON.parse(sessionStorage.getItem("project")).businessUnit;

    const teamResult = await fetch('http://localhost:8080/company/project/teams', {
        method: 'POST',
        headers: {
            'Content-Type': "application/json",
        },
        body: JSON.stringify(project),
        credentials: "include"
    }).catch(error => {
        console.log(error);
        return {
            error: 500
        };
    });

    if(teamResult.status === 200){
        return {
            userBURoles: teamResult.json()
        };
    }

    if(teamResult.status !== 200){
        return {
            error: teamResult.status
        };
    }
}