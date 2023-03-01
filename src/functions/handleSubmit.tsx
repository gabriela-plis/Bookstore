import Book from "../DTO/Book";
import User from "../DTO/User";

function handleSubmit<Type>(DTO: Type, e: React.FormEvent<HTMLFormElement>, url: string): Type[] {
    e.preventDefault();

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


// const handleSubmit = (e: React.FormEvent<HTMLFormElement>, url: string, DTO: Book|User) => {
//     e.preventDefault();

//     fetch(url, {
//         method: 'POST',
//         headers: { "Content-Type": "application/json" },
//         body: JSON.stringify(DTO)
//     })
//     .then( resp => {
//         resp.json()
//     })
//     .then ( data => {
//         return data;
//     });
// }

export default handleSubmit;