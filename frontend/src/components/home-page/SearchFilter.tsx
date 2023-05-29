import BookType from "../../DTO/BookTypeDTO";
import useFetch from "../../functions/useFetch";
import BookSortingCriteria from "../../DTO/BookSortingCriteriaDTO";
import { BOOKS_URL } from "../../variable/constants";
import { useRef } from "react";

type SearchFilterProps = {
    setSearchingCriteria: React.Dispatch<React.SetStateAction<BookSortingCriteria>>
    handleSearch: (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void
    handleReset: (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, ref: React.RefObject<HTMLFormElement>) => void
}

const SearchFilter = (props: SearchFilterProps) => {
    const {setSearchingCriteria, handleReset, handleSearch} = {...props}

    const formRef = useRef<HTMLFormElement>(null);

    return ( 
        <section className="filter">
            <div className="filter__wrapper">
            <h3 className="filter__title">FILTER</h3>
            <form ref={formRef} id="filter-form">
                <TypesSection setSearchingCriteria={setSearchingCriteria} />
                <PublishYearSection setSearchingCriteria={setSearchingCriteria} />
                <section className="filter__button-container">
                    <button className="btn btn--greater" onClick={(e) => handleSearch(e)}>Search</button>
                    <button className="btn btn--greater" onClick={(e) => handleReset(e, formRef)}>Reset</button>
                </section>
            </form>
            </div>
        </section> 
    );
}
 
type setCriteriaProps = {
    setSearchingCriteria: React.Dispatch<React.SetStateAction<BookSortingCriteria>>
}

// types checkbox
const TypesSection = (props: setCriteriaProps) => {
    const {setSearchingCriteria} = {...props}
    
    const types: BookType[] = useFetch(BOOKS_URL + '/types');

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {

        if (e.target.checked) {
            setSearchingCriteria(prevState => (
                {
                    ...prevState,
                    types: new Set(prevState.types?.add(e.target.value))
                }
            )) 
        } else {
            setSearchingCriteria(prevState => {
               const temp = new Set<string>(prevState.types);

               temp.delete(e.target.value)
                
               return {
                ...prevState,
                types: new Set(temp)
               }
            })
        }
    }

    return (
        <section className="filter__types">
            <h4 className="filter__title">Types</h4>
            <ul>
                { types.map( type => (
                <p className="type" key={type.id}>
                    <input 
                        type="checkbox" 
                        name={type.name} 
                        id={type.name} 
                        value={type.name} 
                        onClick={(e) => {
                            handleChange(e as unknown as React.ChangeEvent<HTMLInputElement>)
                        }}
                    />
                    <label htmlFor={type.name}>{type.name}</label>
                </p>
                ))}
            </ul>
        </section>
    )
}


// publish year from-to
const PublishYearSection = (props: setCriteriaProps) => {
    const {setSearchingCriteria} = {...props}

    const minYear = 1950
    const currentYear = new Date().getFullYear()

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchingCriteria(prevState => (
            {
                ...prevState,
                [e.target.name]: e.target.value
            }
        )) 
    }

    return (
        <section className="filter__publishyear">
            <h4 className="filter__title">Publish year</h4>
            <div className="filter__input-container">
                <div className="filter__input-field">
                    <label>From:</label>
                    <input 
                        name="minPublishYear"
                        type="number" 
                        min={minYear} 
                        max={currentYear} 
                        defaultValue={minYear}
                        placeholder={minYear.toString()}
                        onChange={(e) => handleChange(e)}
                    />
                </div>
                <div className="filter__input-field">
                    <label>To:</label>
                    <input 
                        name="maxPublishYear"
                        type="number" 
                        min="1950" 
                        max={currentYear} 
                        defaultValue={currentYear}
                        placeholder={currentYear.toString()}
                        onChange={(e) => handleChange(e)}
                    /> 
                </div>
            </div>
        </section>
    )
}

export default SearchFilter;