function appendParamsToUrl(urlBeginning: string, params: Map<string,string|number|Set<string>> ): string {

    const url = new URL(urlBeginning);

    params.forEach((value,key) => {
      if (value instanceof Set) {
        const combinedValues: string = Array.from(value).toString()
        url.searchParams.append(key, combinedValues.toString()) 
      } else {
        url.searchParams.append(key, value.toString()) 
      }

    })
    
    return decodeURIComponent(url.toString());
  }

export default appendParamsToUrl;