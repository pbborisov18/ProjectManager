//This should handle stuff on the server side. But how tf do I call it on the client side
//before the component loads. pretty much useless cuz if I figure that out I ca
export async function post(request) {
    try {
        const data = await request.json(); // get the object from the request body
        console.log(data); // log the object to the console

        return {
            body: JSON.stringify({ message: "Object received!" }),
        };
    } catch (error) {
        console.error(error); // log the error to the console

        return {
            status: 400, // return a 400 Bad Request status code
            body: JSON.stringify({ error: "Invalid request body" }),
        };
    }
}