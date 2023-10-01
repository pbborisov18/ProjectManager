export async function load({fetch}) {
    let company = JSON.parse(sessionStorage.getItem("company")).businessUnit;

    if(company.whiteboard.id) {
        return {whiteboard: company.whiteboard};
    }

    const whiteboardResult = await fetch('http://localhost:8080/company/whiteboard', {
        method: 'POST',
        headers: {
            'Content-Type': "application/json",
        },
        body: JSON.stringify(company),
        credentials: "include"
    }).catch(error => {
        return {
            error: 500
        };
    });

    if(whiteboardResult.status === 200) {
        return{
            whiteboard: whiteboardResult.json()
        }
    }
}


