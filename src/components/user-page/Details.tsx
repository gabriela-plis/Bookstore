import User from "../../DTO/User";
import useFetch from "../../functions/useFetch";


const Details = () => {
    const user: User = useFetch('http://localhost:8000/users/' + sessionStorage.getItem("id")) as unknown as User;

    return (
        <section className="details">
            <h2 className="details__title">Account details</h2>
            <p className="details__data details__data--first-name">First name: <span className="details__field">{user.firstName}</span></p>
            <p className="details__data details__data--last-name">Last name: <span className="details__field">{user.lastName}</span></p>
            <p className="details__data details__data--email">Email: <span className="details__field">{user.email}</span></p>
            <p className="details__data details__data--phone">Phone: 
                {user.phone ? <span className="details__field">{user.phone}</span> : <span className="details__field">- - -</span>}
            </p>
            <span className="details__image"><div></div></span>
        </section>
    )
}

export default Details;