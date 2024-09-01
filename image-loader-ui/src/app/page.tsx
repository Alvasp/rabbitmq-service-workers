import Image from "next/image";
import { Fragment } from "react";
import PageClient from "./lib/features/images-processing/components/page-client";

export default async function Home() {

  return (
    <Fragment>
      <PageClient />
    </Fragment>
  );
}
