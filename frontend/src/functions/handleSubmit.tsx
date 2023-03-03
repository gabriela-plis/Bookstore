import Book from "../DTO/Book";
import User from "../DTO/User";

//arg for error
function handleSubmit<Type>(DTO: Type, url: string, needObject: boolean): Type[] {

    fetch(url, {
        method: 'POST',
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(DTO)
    })
    .then( resp => {
        resp.json()
    })
    .then ( data => {
        return data;
    });

    return [];

  }

export default handleSubmit;