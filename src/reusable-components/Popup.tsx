import Book from "../DTO/Book";

type Props = {
    title: string;
    book: Book;
    handleOperation: () => void;
    setIsOperationActive: React.Dispatch<React.SetStateAction<boolean>>;
}

const Popup = (props: Props) => {

    const {title, book, handleOperation, setIsOperationActive} = {...props};

    const handleCancelOperation = () => {
        setIsOperationActive(false);
    }

    return (  
        <div className="popup__background">
            <section className="popup__container">
                <h2 className="popup__title">{title}</h2>
                <div className="popup__book">
                    <h3>{book.title}</h3>
                    <p>Author: {book.author}  Publish year: {book.publishYear}  Type: {book.type}</p>
                </div>
                <button className="btn btn--cancel" onClick={handleOperation}>Cancel</button>
                <button className="btn btn--save" onClick={handleCancelOperation}>{title}</button>
            </section>
        </div>
    );
}
 
export default Popup;