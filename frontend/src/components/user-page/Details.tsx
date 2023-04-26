import User from "../../DTO/UserDTO";
import useFetch from "../../functions/useFetch";


const Details = () => {
    const user: User = useFetch('http://localhost:8080/users/this') as unknown as User;

    const userData = [
        {title: "First name", className:"first-name", value: user.firstName},
        {title: "Last name", className:"last-name", value: user.lastName},
        {title: "Email", className:"email", value: user.email},
        {title: "Phone", className:"phone", value: user.phone}
    ]

    const conditionalRender = (value: string, title: string) => {
        if (value) {
            return <span>{title}: <span className="details__field">{value}</span></span>
        } else {
            return <span>{title}: <span className="details__field">---</span></span>
        }
    }

    return (
        <section className="details">
            <h2 className="details__title">Account details</h2>
            {userData.map(data => (
                <p
                    key={data.className}
                    className={`details__data details__data--${data.className}`}
                >
                    {conditionalRender(data.value, data.title)}
                </p>
            ))}
            <span className="details__image"><div></div></span>
        </section>
    )
}

export default Details;