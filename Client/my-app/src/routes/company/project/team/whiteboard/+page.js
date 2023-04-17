export async function load({fetch}) {
    let team = JSON.parse(sessionStorage.getItem("team")).businessUnit;

    console.log(team);

    const whiteboardResult = await fetch('http://localhost:8080/company/project/whiteboard', {
        method: 'POST',
        headers: {
            'Content-Type': "application/json",
        },
        body: JSON.stringify(team),
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
