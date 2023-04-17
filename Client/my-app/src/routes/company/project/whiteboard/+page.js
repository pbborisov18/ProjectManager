export async function load({fetch}) {
    let project = JSON.parse(sessionStorage.getItem("project")).businessUnit;

    console.log(project);

    const whiteboardResult = await fetch('http://localhost:8080/company/project/team/whiteboard', {
        method: 'POST',
        headers: {
            'Content-Type': "application/json",
        },
        body: JSON.stringify(project),
        credentials: "include"
    }).catch(error => {
        return {
            error: 500
        };
    });

    if (whiteboardResult.status === 200) {
        return{
            whiteboard: whiteboardResult.json()
        }
    }
}
